package com.dust.courseRegistration.service;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dust.courseRegistration.dto.request.TeacherCreateRequest;
import com.dust.courseRegistration.dto.request.TeacherUpdateRequest;
import com.dust.courseRegistration.entity.Teacher;
import com.dust.courseRegistration.exception.AppException;
import com.dust.courseRegistration.exception.ErrorCode;
import com.dust.courseRegistration.mapper.TeacherMapper;
import com.dust.courseRegistration.repository.TeacherRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TeacherService {

    TeacherRepository tchRepo;

    TeacherMapper tchMapper;

    @Transactional
    @Cacheable(value = "teachers", key = "#teacherId")
    public Teacher getTeacher(String teacherId) {
        return tchRepo.findById(teacherId).orElseThrow(() -> new AppException(ErrorCode.ITEM_NOT_EXISTED));
    }

    @Transactional
    public List<Teacher> getTeacherList() {
        return tchRepo.findAll();
    }

    @Transactional
    @Cacheable(value = "teachers", key = "#request.username()")
    public Teacher createTeacher(TeacherCreateRequest request) {
        if (tchRepo.existsById(request.username())) throw new AppException(ErrorCode.ITEM_EXISTED);
        Teacher teacher = tchMapper.toTeacher(request);
        return tchRepo.save(teacher);
    }

    @Transactional
    @CachePut(value = "teachers", key = "#teacherId")
    public Teacher updateTeacher(String teacherId, TeacherUpdateRequest request) {
        var tch = getTeacher(teacherId);
        tchMapper.updateTeacherFromRequest(tch, request);
        return tchRepo.save(tch);
    }

    @Transactional
    @CacheEvict(value = "teachers", key = "#teacherId")
    public void removeTeacher(String teacherId) {
        tchRepo.deleteById(teacherId);
    }
}
