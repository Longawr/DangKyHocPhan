package com.dust.courseRegistration.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dust.courseRegistration.dto.request.SubjectCreateRequest;
import com.dust.courseRegistration.dto.request.SubjectUpdateRequest;
import com.dust.courseRegistration.dto.response.ApiResponse;
import com.dust.courseRegistration.dto.response.SubjectResponse;
import com.dust.courseRegistration.entity.Subject;
import com.dust.courseRegistration.mapper.SubjectMapper;
import com.dust.courseRegistration.service.SubjectService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/subjects")
public class SubjectController {

    SubjectMapper sbjMapper;

    SubjectService sbjService;

    @GetMapping
    @PreAuthorize("hasRole('GV')")
    public ApiResponse<List<SubjectResponse>> getSubjectList() {

        List<Subject> subjects = sbjService.getSubjectList();
        return ApiResponse.<List<SubjectResponse>>builder()
                .result(subjects.stream().map(sbjMapper::toSubjectResponse).toList())
                .build();
    }

    @GetMapping("/id/{subjectId}")
    @PreAuthorize("hasRole('GV')")
    public ApiResponse<SubjectResponse> getSubjectByID(@PathVariable String subjectId) {

        Subject sbj = sbjService.getSubject(subjectId);
        return ApiResponse.<SubjectResponse>builder()
                .result(sbjMapper.toSubjectResponse(sbj))
                .build();
    }

    @PostMapping("/save")
    @PreAuthorize("hasRole('GV')")
    public ApiResponse<SubjectResponse> addSubject(@Valid @RequestBody SubjectCreateRequest request) {

        Subject sbj = sbjService.createSubject(request);
        return ApiResponse.<SubjectResponse>builder()
                .result(sbjMapper.toSubjectResponse(sbj))
                .build();
    }

    @PutMapping("/update/{subjectId}")
    @PreAuthorize("hasRole('GV')")
    public ApiResponse<SubjectResponse> updateSubject(
            @PathVariable String subjectId, @Valid @RequestBody SubjectUpdateRequest request) {

        Subject sbj = sbjService.updateSubject(subjectId, request);
        return ApiResponse.<SubjectResponse>builder()
                .result(sbjMapper.toSubjectResponse(sbj))
                .build();
    }

    @DeleteMapping("/delete/{subjectId}")
    @PreAuthorize("hasRole('GV')")
    public ApiResponse<Void> removeSubject(@PathVariable String subjectId) {

        sbjService.removeSubject(subjectId);
        return ApiResponse.<Void>builder().message("Deleted subject successful").build();
    }
}
