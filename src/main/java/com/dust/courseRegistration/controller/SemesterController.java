package com.dust.courseRegistration.controller;

import java.time.LocalDate;
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

import com.dust.courseRegistration.dto.request.SemesterCreateRequest;
import com.dust.courseRegistration.dto.response.ApiResponse;
import com.dust.courseRegistration.dto.response.SemesterResponse;
import com.dust.courseRegistration.entity.Semester;
import com.dust.courseRegistration.mapper.SemesterMapper;
import com.dust.courseRegistration.service.SemesterService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/semesters")
public class SemesterController {

    SemesterMapper semMapper;

    SemesterService semService;

    @GetMapping
    public ApiResponse<List<SemesterResponse>> getSemesterList() {

        List<Semester> sem = semService.getSemesterList();
        return ApiResponse.<List<SemesterResponse>>builder()
                .result(sem.stream().map(semMapper::toSemesterResponse).toList())
                .build();
    }

    @GetMapping("/id")
    public ApiResponse<SemesterResponse> getSemesterByID(@RequestParam int year, @RequestParam int id) {

        Semester sem = semService.getSemester(id, year);
        return ApiResponse.<SemesterResponse>builder()
                .result(semMapper.toSemesterResponse(sem))
                .build();
    }

    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<SemesterResponse> addSemester(@Valid @RequestBody SemesterCreateRequest request) {

        Semester sem = semService.createSemester(request);
        return ApiResponse.<SemesterResponse>builder()
                .result(semMapper.toSemesterResponse(sem))
                .build();
    }

    @PostMapping("/save-this-semester")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<SemesterResponse> addThisSemester() {
        var request = SemesterCreateRequest.of(LocalDate.now());
        Semester sem = semService.createSemester(request);
        return ApiResponse.<SemesterResponse>builder()
                .result(semMapper.toSemesterResponse(sem))
                .build();
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> removeSemesterByID(@RequestParam int year, @RequestParam int id) {

        semService.removeSemester(id, year);
        return ApiResponse.<Void>builder()
                .message("Deleted semester successful")
                .build();
    }
}
