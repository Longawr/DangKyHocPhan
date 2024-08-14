package com.dust.courseRegistration.service;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dust.courseRegistration.dto.request.StudentCreateRequest;
import com.dust.courseRegistration.dto.request.StudentUpdateRequest;
import com.dust.courseRegistration.entity.ClassName;
import com.dust.courseRegistration.entity.Student;
import com.dust.courseRegistration.exception.AppException;
import com.dust.courseRegistration.exception.ErrorCode;
import com.dust.courseRegistration.mapper.StudentMapper;
import com.dust.courseRegistration.repository.StudentRepository;

import io.micrometer.common.util.StringUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StudentService {

    StudentRepository stdRepo;

    StudentMapper stdMapper;

    ClassNameService clsService;

    @Transactional
    public boolean checkStudentByClassName(String username, String classId) {
        return stdRepo.existsByUsernameAndClassId(username, classId);
    }

    @Transactional
    @Cacheable(value = "students", key = "#studentId")
    public Student getStudent(String studentId) {

        return stdRepo.findById(studentId).orElseThrow(() -> new AppException(ErrorCode.ITEM_NOT_EXISTED));
    }

    @Transactional
    @Cacheable(value = "classesMember", key = "#classId")
    public List<Student> getStudentListByClass(String classId) {
        return stdRepo.findByClassId(classId);
    }

    @Transactional
    @Cacheable(value = "students", key = "#request.username()")
    public Student createStudent(StudentCreateRequest request) {
        var cls = new ClassName();

        if (stdRepo.existsById(request.username())) throw new AppException(ErrorCode.ITEM_EXISTED);

        Student std = stdMapper.toStudent(request);

        if (StringUtils.isNotBlank(request.classNameId())) {
            cls = clsService.getClassName(request.classNameId());
            std.setClassName(cls);

            std = stdRepo.save(std);
            clsService.updateClassMemberCount(cls);
        } else {
            std = stdRepo.save(std);
        }
        return std;
    }

    @Transactional
    @CachePut(value = "students", key = "#username")
    public Student updateStudent(String username, StudentUpdateRequest request, boolean isAdmin) {
        Student student = getStudent(username);
        stdMapper.updateStudentFromRequest(student, request);

        ClassName oldClass = student.getClassName();
        String newClassNameId = request.classNameId();

        boolean isClassNameChange = (oldClass == null && newClassNameId != null)
                || (oldClass != null && !oldClass.getId().equals(newClassNameId));

        if (isAdmin && isClassNameChange) {

            ClassName newClass = newClassNameId != null ? clsService.getClassName(newClassNameId) : null;
            student.setClassName(newClass);
            student = stdRepo.save(student);

            if (oldClass != null) {
                clsService.updateClassMemberCount(oldClass);
            }
            if (newClass != null) {
                clsService.updateClassMemberCount(newClass);
            }
        } else {
            stdRepo.save(student);
        }

        return student;
    }

    @Transactional
    @CacheEvict(value = "students", key = "#studentId")
    public void removeStudent(String studentId) {
        stdRepo.deleteById(studentId);
    }
}
