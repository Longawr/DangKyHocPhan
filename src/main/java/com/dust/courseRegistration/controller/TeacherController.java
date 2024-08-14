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

import com.dust.courseRegistration.dto.request.TeacherCreateRequest;
import com.dust.courseRegistration.dto.request.TeacherUpdateRequest;
import com.dust.courseRegistration.dto.response.ApiResponse;
import com.dust.courseRegistration.dto.response.TeacherResponse;
import com.dust.courseRegistration.entity.Teacher;
import com.dust.courseRegistration.mapper.TeacherMapper;
import com.dust.courseRegistration.service.TeacherService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/teachers")
public class TeacherController {

    TeacherMapper tchMapper;

    TeacherService tchService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<TeacherResponse>> getTeacherList() {

        List<Teacher> teachers = tchService.getTeacherList();
        return ApiResponse.<List<TeacherResponse>>builder()
                .result(teachers.stream().map(tchMapper::toTeacherResponse).toList())
                .build();
    }

    @GetMapping("/username/{username}")
    @PreAuthorize("hasRole('ADMIN') or #username == authentication.name")
    public ApiResponse<TeacherResponse> getTeacher(@PathVariable String username) {

        Teacher tch = tchService.getTeacher(username);
        return ApiResponse.<TeacherResponse>builder()
                .result(tchMapper.toTeacherResponse(tch))
                .build();
    }

    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<TeacherResponse> addTeacher(@Valid @RequestBody TeacherCreateRequest request) {

        Teacher tch = tchService.createTeacher(request);
        return ApiResponse.<TeacherResponse>builder()
                .result(tchMapper.toTeacherResponse(tch))
                .build();
    }

    @PutMapping("/update/{teacherId}")
    @PreAuthorize("hasRole('ADMIN') or #teacherId == authentication.name")
    public ApiResponse<TeacherResponse> updateSubject(
            @PathVariable String teacherId, @Valid @RequestBody TeacherUpdateRequest request) {

        Teacher tch = tchService.updateTeacher(teacherId, request);
        return ApiResponse.<TeacherResponse>builder()
                .result(tchMapper.toTeacherResponse(tch))
                .build();
    }

    @DeleteMapping("/delete/{teacherId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> removeTeacher(@PathVariable String teacherId) {

        tchService.removeTeacher(teacherId);
        return ApiResponse.<Void>builder().message("Deleted teacher successful").build();
    }
}
