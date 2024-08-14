package com.dust.courseRegistration.service;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dust.courseRegistration.dto.request.ClassnameCreateRequest;
import com.dust.courseRegistration.dto.request.ClassnameUpdateRequest;
import com.dust.courseRegistration.entity.ClassName;
import com.dust.courseRegistration.enums.SexType;
import com.dust.courseRegistration.exception.AppException;
import com.dust.courseRegistration.exception.ErrorCode;
import com.dust.courseRegistration.mapper.ClassNameMapper;
import com.dust.courseRegistration.repository.ClassNameRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ClassNameService {

    ClassNameRepository clsRepo;

    ClassNameMapper clsMapper;

    @Transactional
    public List<ClassName> getClassList() {
        return clsRepo.findAll();
    }

    @Transactional
    @Cacheable(value = "classes", key = "#classId")
    public ClassName getClassName(String classId) {
        return clsRepo.findById(classId).orElseThrow(() -> new AppException(ErrorCode.ITEM_NOT_EXISTED));
    }

    @Transactional
    public int getClassMemberCount(String id) {
        return clsRepo.countStudentsByClassId(id);
    }

    @Transactional
    public int getClassMaleCount(String id) {
        return clsRepo.countGenderStudentsByClassId(id, SexType.NAM);
    }

    @Transactional
    public int getClassFemaleCount(String id) {
        return clsRepo.countGenderStudentsByClassId(id, SexType.NU);
    }

    @Transactional
    public ClassName createClass(@Valid ClassnameCreateRequest request) {
        if (clsRepo.existsById(request.id())) throw new AppException(ErrorCode.ITEM_EXISTED);
        ClassName className = clsMapper.toClassName(request);
        return clsRepo.save(className);
    }

    @Transactional
    @CachePut(value = "classes", key = "#id")
    public ClassName updateClass(String id, @Valid ClassnameUpdateRequest cls) {
        ClassName className = clsRepo.findById(id).orElseThrow(() -> new AppException(ErrorCode.ITEM_NOT_EXISTED));
        clsMapper.updateClassNameFromRequest(className, cls);
        return clsRepo.save(className);
    }

    @Transactional
    @CachePut(value = "classes", key = "#cls.getId()")
    public ClassName updateClassMemberCount(ClassName cls) {
        cls.setTotal(getClassMemberCount(cls.getId()));
        cls.setMaleCount(getClassMaleCount(cls.getId()));
        return clsRepo.save(cls);
    }

    @Transactional
    @CacheEvict(value = "classes", key = "#id")
    public void removeClassByID(String id) {
        clsRepo.deleteById(id);
    }
}
