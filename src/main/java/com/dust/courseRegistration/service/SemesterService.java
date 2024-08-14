package com.dust.courseRegistration.service;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dust.courseRegistration.dto.request.SemesterCreateRequest;
import com.dust.courseRegistration.entity.Semester;
import com.dust.courseRegistration.entity.SemesterId;
import com.dust.courseRegistration.exception.AppException;
import com.dust.courseRegistration.exception.ErrorCode;
import com.dust.courseRegistration.mapper.SemesterMapper;
import com.dust.courseRegistration.repository.SemesterRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SemesterService {

    SemesterRepository semRepo;

    SemesterMapper semMapper;

    @Transactional
    public List<Semester> getSemesterList() {
        return semRepo.findAll();
    }

    @Transactional
    @Cacheable(value = "semesters", key = "#semesterId + '-' + #semesterYear")
    public Semester getSemester(int semesterId, int semesterYear) {
        return semRepo.findById(new SemesterId(semesterId, semesterYear))
                .orElseThrow(() -> new AppException(ErrorCode.ITEM_NOT_EXISTED));
    }

    @Transactional
    public boolean existSemester(int id, int year) {
        return semRepo.existsById(new SemesterId(id, year));
    }

    @Transactional
    @Cacheable(value = "semesters", key = "#request.id() + '-' + #request.year()")
    public Semester createSemester(SemesterCreateRequest request) {

        Semester sem = semMapper.toSemester(request);
        if (semRepo.existsById(sem.getIds())) throw new AppException(ErrorCode.ITEM_EXISTED);

        return semRepo.save(sem);
    }

    @Transactional
    @CacheEvict(value = "semesters", key = "#id + '-' + #year")
    public void removeSemester(int id, int year) {
        semRepo.deleteById(new SemesterId(id, year));
    }
}
