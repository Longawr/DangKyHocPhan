package com.dust.courseRegistration.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dust.courseRegistration.dto.request.StudentCreateRequest;
import com.dust.courseRegistration.dto.request.StudentUpdateRequest;
import com.dust.courseRegistration.dto.response.ApiResponse;
import com.dust.courseRegistration.dto.response.StudentPublicResponse;
import com.dust.courseRegistration.dto.response.StudentResponse;
import com.dust.courseRegistration.entity.Student;
import com.dust.courseRegistration.exception.AppException;
import com.dust.courseRegistration.exception.ErrorCode;
import com.dust.courseRegistration.mapper.StudentMapper;
import com.dust.courseRegistration.service.StudentService;
import com.dust.courseRegistration.util.RoleCheckUtils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/students")
public class StudentController {

    StudentMapper stdMapper;

    StudentService stdService;

    @GetMapping("/classid/{classId}")
    @PreAuthorize("hasRole('SV')")
    public ApiResponse<List<StudentPublicResponse>> getStudentListByClass(
            @PathVariable String classId, Authentication authentication) {

        String username = authentication.getName();

        if (!(RoleCheckUtils.isAdmin(authentication) || stdService.checkStudentByClassName(username, classId))) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        List<Student> students = stdService.getStudentListByClass(classId);
        return ApiResponse.<List<StudentPublicResponse>>builder()
                .result(students.stream()
                        .map(stdMapper::toStudentPublicResponse)
                        .toList())
                .build();
    }

    @GetMapping("/name/{username}")
    @PreAuthorize("hasRole('ADMIN') or #username == authentication.name")
    public ApiResponse<StudentResponse> getStudent(@PathVariable String username) {

        Student std = stdService.getStudent(username);
        return ApiResponse.<StudentResponse>builder()
                .result(stdMapper.toStudentResponse(std))
                .build();
    }

    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<StudentResponse> addStudent(@Valid @RequestBody StudentCreateRequest request) {

        Student std = stdService.createStudent(request);

        return ApiResponse.<StudentResponse>builder()
                .result(stdMapper.toStudentResponse(std))
                .build();
    }

    @PutMapping("/update/{username}")
    @PreAuthorize("hasRole('ADMIN') or #username == authentication.name")
    public ApiResponse<StudentResponse> updateStudent(
            @PathVariable String username,
            @Valid @RequestBody StudentUpdateRequest request,
            Authentication authentication) {
        boolean isAdmin = RoleCheckUtils.isAdmin(authentication);
        Student std = stdService.updateStudent(username, request, isAdmin);
        return ApiResponse.<StudentResponse>builder()
                .result(stdMapper.toStudentResponse(std))
                .build();
    }

    @DeleteMapping("/delete/{studentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> removeStudent(@PathVariable String studentId) {

        stdService.removeStudent(studentId);
        return ApiResponse.<Void>builder().message("Deleted student successful").build();
    }
}
