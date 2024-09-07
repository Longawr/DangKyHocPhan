package com.dust.courseRegistration.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dust.courseRegistration.dto.request.CourseStudentCreateRequest;
import com.dust.courseRegistration.entity.Course;
import com.dust.courseRegistration.entity.CourseStudent;
import com.dust.courseRegistration.entity.CourseStudentId;
import com.dust.courseRegistration.entity.SemesterId;
import com.dust.courseRegistration.entity.Student;
import com.dust.courseRegistration.exception.AppException;
import com.dust.courseRegistration.exception.ErrorCode;
import com.dust.courseRegistration.repository.CourseStudentRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CourseStudentService {

    CourseStudentRepository csRepo;

    CourseService crsService;
    StudentService stdService;

    @Transactional
    public List<CourseStudent> getCoursesByStudent(String studentId, int semesterId, int semesterYear) {
        var ids = new SemesterId(semesterId, semesterYear);
        return csRepo.findAllByStudentIdAndSemesterId(studentId, ids);
    }

    @Transactional
    public List<CourseStudent> getStudentsByCourse(int courseId) {
        return csRepo.findAllByCourseId(courseId);
    }

    @Transactional
    public int getCoursesCountByStudent(String studentId, int semesterId, int semesterYear) {

        var ids = new SemesterId(semesterId, semesterYear);
        return csRepo.countByStudentIdAndSemesterId(studentId, ids);
    }

    @Transactional
    public int getCoursesCountByStudent(String studentId) {
        return csRepo.countByStudentId(studentId);
    }

    @Transactional
    public CourseStudent getCourseStudent(String studentId, int courseId) {
        return csRepo.findById(new CourseStudentId(studentId, courseId))
                .orElseThrow(() -> new AppException(ErrorCode.ITEM_NOT_EXISTED));
    }

    @Transactional
    public CourseStudent createCourseStudent(CourseStudentCreateRequest request) {

        Course crs = crsService.getCourse(request.courseId());
        if (crs.getRegisterSlot() >= crs.getMaxSlot()) {
            throw new AppException(ErrorCode.COURSE_FULL);
        }
        Student std = stdService.getStudent(request.studentId());

        var cs = CourseStudent.builder()
                .ids(new CourseStudentId(request.studentId(), request.courseId()))
                .course(crs)
                .student(std)
                .build();

        cs = csRepo.save(cs);
        crsService.updateCourseMemberCount(crs);

        return cs;
    }

    @Transactional
    public void removeCourseStudent(int courseId, String studentId, boolean isAdmin) {

        var ids = new CourseStudentId(studentId, courseId);
        if (!(isAdmin || csRepo.ExistsByStudentIdAndCourseIdWithActivePeriod(ids, LocalDate.now()))) {
            throw new AppException(ErrorCode.NOT_ACCESSABLE);
        }
        Course crs = crsService.getCourse(courseId);
        csRepo.deleteById(ids);
        crsService.updateCourseMemberCount(crs);
    }
}
