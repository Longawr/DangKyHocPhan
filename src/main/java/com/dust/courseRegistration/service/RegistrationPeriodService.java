package com.dust.courseRegistration.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dust.courseRegistration.dto.request.RegistrationPeriodCreateRequest;
import com.dust.courseRegistration.entity.RegistrationPeriod;
import com.dust.courseRegistration.entity.RegistrationPeriodId;
import com.dust.courseRegistration.entity.Semester;
import com.dust.courseRegistration.exception.AppException;
import com.dust.courseRegistration.exception.ErrorCode;
import com.dust.courseRegistration.mapper.RegistrationPeriodMapper;
import com.dust.courseRegistration.repository.RegistrationPeriodRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RegistrationPeriodService {

    RegistrationPeriodRepository rpRepo;
    RegistrationPeriodMapper rpMapper;
    SemesterService semService;

    @Transactional
    public List<RegistrationPeriod> getRegistrationPeriodList() {
        return rpRepo.findAll();
    }

    @Transactional
    public RegistrationPeriod getRegistrationPeriod(int id, int semesterId, int year) {
        return rpRepo.findById(new RegistrationPeriodId(id, semesterId, year))
                .orElseThrow(() -> new AppException(ErrorCode.ITEM_NOT_EXISTED));
    }

    @Transactional
    public List<RegistrationPeriod> getRegistrationPeriodBySemester(int semesterId, int semesterYear) {
        return rpRepo.findBySemesterIds(semesterId, semesterYear);
    }

    @Transactional
    public RegistrationPeriod createRegistrationPeriod(RegistrationPeriodCreateRequest request) {
        Semester sem = semService.getSemester(request.semesterId(), request.year());
        RegistrationPeriod rp = rpMapper.toRegistrationPeriod(request);
        rp.setSemester(sem);
        return rpRepo.save(rp);
    }

    @Transactional
    public void removeRegistrationPeriod(int id, int semesterId, int year) {
        rpRepo.deleteById(new RegistrationPeriodId(id, semesterId, year));
    }
}
