package com.dust.courseRegistration.service;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dust.courseRegistration.dto.request.SubjectCreateRequest;
import com.dust.courseRegistration.dto.request.SubjectUpdateRequest;
import com.dust.courseRegistration.entity.Subject;
import com.dust.courseRegistration.exception.AppException;
import com.dust.courseRegistration.exception.ErrorCode;
import com.dust.courseRegistration.mapper.SubjectMapper;
import com.dust.courseRegistration.repository.SubjectRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SubjectService {

    SubjectRepository sbjRepo;

    SubjectMapper sbjMapper;

    @Transactional
    public Subject getSubject(String subjectId) {
        return sbjRepo.findById(subjectId).orElseThrow(() -> new AppException(ErrorCode.ITEM_NOT_EXISTED));
    }

    @Transactional
    public List<Subject> getSubjectList() {
        return sbjRepo.findAll();
    }

    @Transactional
    public Subject createSubject(SubjectCreateRequest request) {
        Subject sbj = sbjMapper.toSubject(request);
        return sbjRepo.save(sbj);
    }

    @Transactional
    public Subject updateSubject(String subjectId, @Valid SubjectUpdateRequest request) {
        Subject sbj = getSubject(subjectId);
        sbjMapper.updateSubjectFromRequest(sbj, request);
        return sbjRepo.save(sbj);
    }

    @Transactional
    public void removeSubject(String subjectID) {
        sbjRepo.deleteById(subjectID);
    }
}
