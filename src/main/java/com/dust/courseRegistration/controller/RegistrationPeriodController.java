package com.dust.courseRegistration.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dust.courseRegistration.dto.request.RegistrationPeriodCreateRequest;
import com.dust.courseRegistration.dto.response.ApiResponse;
import com.dust.courseRegistration.dto.response.RegistrationPeriodResponse;
import com.dust.courseRegistration.entity.RegistrationPeriod;
import com.dust.courseRegistration.mapper.RegistrationPeriodMapper;
import com.dust.courseRegistration.service.RegistrationPeriodService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/courseregistrations")
public class RegistrationPeriodController {

    RegistrationPeriodMapper rpMapper;

    RegistrationPeriodService rpService;

    @GetMapping
    public ApiResponse<List<RegistrationPeriodResponse>> getRegistrationPeriodList() {

        List<RegistrationPeriod> rp = rpService.getRegistrationPeriodList();
        return ApiResponse.<List<RegistrationPeriodResponse>>builder()
                .result(rp.stream().map(rpMapper::toRegistrationPeriodResponse).toList())
                .build();
    }

    @GetMapping("/semester")
    public ApiResponse<List<RegistrationPeriodResponse>> getRegistrationPeriodBySemester(
            @RequestParam int semesterId, @RequestParam int year) {

        List<RegistrationPeriod> rp = rpService.getRegistrationPeriodBySemester(semesterId, year);
        return ApiResponse.<List<RegistrationPeriodResponse>>builder()
                .result(rp.stream().map(rpMapper::toRegistrationPeriodResponse).toList())
                .build();
    }

    @GetMapping("/id")
    public ApiResponse<RegistrationPeriodResponse> getRegistrationPeriod(
            @RequestParam int id, @RequestParam int semesterId, @RequestParam int year) {

        RegistrationPeriod rp = rpService.getRegistrationPeriod(id, semesterId, year);
        return ApiResponse.<RegistrationPeriodResponse>builder()
                .result(rpMapper.toRegistrationPeriodResponse(rp))
                .build();
    }

    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<RegistrationPeriodResponse> createRegistrationPeriod(
            @Valid @RequestBody RegistrationPeriodCreateRequest request) {

        RegistrationPeriod rp = rpService.createRegistrationPeriod(request);
        return ApiResponse.<RegistrationPeriodResponse>builder()
                .result(rpMapper.toRegistrationPeriodResponse(rp))
                .build();
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> removeRegistrationPeriod(
            @RequestParam int id, @RequestParam int semesterId, @RequestParam int year) {

        rpService.removeRegistrationPeriod(id, semesterId, year);
        return ApiResponse.<Void>builder()
                .message("Deleted registration period successful")
                .build();
    }
}
