package com.dust.courseRegistration.service;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dust.courseRegistration.dto.request.CourseCreateRequest;
import com.dust.courseRegistration.dto.request.CourseUpdateRequest;
import com.dust.courseRegistration.entity.Course;
import com.dust.courseRegistration.entity.Semester;
import com.dust.courseRegistration.entity.Subject;
import com.dust.courseRegistration.entity.Teacher;
import com.dust.courseRegistration.exception.AppException;
import com.dust.courseRegistration.exception.ErrorCode;
import com.dust.courseRegistration.mapper.CourseMapper;
import com.dust.courseRegistration.repository.CourseRepository;

import io.micrometer.common.util.StringUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CourseService {

    CourseRepository crsRepo;

    CourseMapper crsMapper;

    SemesterService smtService;

    @Transactional
    public List<Course> getCourses() {
        return crsRepo.findAll();
    }

    @Transactional
    public List<Course> getCoursesBySemester(int semesterId, int semesterYear) {
        var smt = smtService.getSemester(semesterId, semesterYear);
        return crsRepo.findBySemester(smt);
    }

    @Transactional
    @Cacheable(value = "courses", key = "#courseId")
    public Course getCourse(int courseId) {
        return crsRepo.findById(courseId).orElseThrow(() -> new AppException(ErrorCode.ITEM_NOT_EXISTED));
    }

    @Transactional
    public int getCourseMemberCount(int courseId) {
        return crsRepo.countStudentByCourseId(courseId);
    }

    @Transactional
    public boolean checkCourseExistByTeacher(int courseId, String teacherId) {
        Teacher tch = new Teacher(teacherId);
        return crsRepo.existsByIdAndTeacher(courseId, tch);
    }

    @Transactional
    public Course createCourse(CourseCreateRequest request) {
        int semesterId = request.semesterId();
        int semesterYear = request.semesterYear();
        String subjectId = request.subjectId();
        String teacherId = request.teacherId();

        Course course = crsMapper.toCourse(request);

        if (semesterId != 0 && semesterYear != 0) {
            course.setSemester(new Semester(semesterId, semesterYear));
        }
        if (StringUtils.isNotBlank(subjectId)) {
            course.setSubject(new Subject(subjectId.strip()));
        }
        if (StringUtils.isNotBlank(teacherId)) {
            course.setTeacher(new Teacher(teacherId.strip()));
        }

        return crsRepo.save(course);
    }

    @Transactional
    @CachePut(value = "courses", key = "#courseId")
    public Course updateCourse(int courseId, CourseUpdateRequest request, boolean isAdmin) {
        int semesterId = request.semesterId();
        int semesterYear = request.semesterYear();
        String subjectId = request.subjectId();
        String teacherId = request.teacherId();
        Course course = getCourse(courseId);
        crsMapper.updateCourseFromRequest(course, request);

        if (isAdmin) {
            if (semesterId != 0 && semesterYear != 0) {
                course.setSemester(new Semester(semesterId, semesterYear));
            }
            if (StringUtils.isNotBlank(subjectId)) {
                course.setSubject(new Subject(subjectId.strip()));
            }
            if (StringUtils.isNotBlank(teacherId)) {
                course.setTeacher(new Teacher(teacherId.strip()));
            }
        }

        return crsRepo.save(course);
    }

    @Transactional
    @CachePut(value = "courses", key = "#crs.getId()")
    Course updateCourseMemberCount(Course crs) {
        crs.setRegisterSlot(getCourseMemberCount(crs.getId()));
        return crsRepo.save(crs);
    }

    @Transactional
    @CacheEvict(value = "courses", key = "#courseId")
    public void removeCourseByID(int courseId) {
        crsRepo.deleteById(courseId);
    }
}
