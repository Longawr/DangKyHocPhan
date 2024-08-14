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

import com.dust.courseRegistration.dto.request.ClassnameCreateRequest;
import com.dust.courseRegistration.dto.request.ClassnameUpdateRequest;
import com.dust.courseRegistration.dto.response.ApiResponse;
import com.dust.courseRegistration.dto.response.ClassNameResponse;
import com.dust.courseRegistration.entity.ClassName;
import com.dust.courseRegistration.mapper.ClassNameMapper;
import com.dust.courseRegistration.service.ClassNameService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/classes")
public class ClassNameController {

    ClassNameMapper clsMapper;

    ClassNameService clsService;

    @GetMapping
    @PreAuthorize("hasRole('GV')")
    public ApiResponse<List<ClassNameResponse>> getAll() {

        List<ClassName> classes = clsService.getClassList();
        return ApiResponse.<List<ClassNameResponse>>builder()
                .result(classes.stream().map(clsMapper::toClassNameResponse).toList())
                .build();
    }

    @GetMapping("/total/{id}")
    public ApiResponse<Integer> getTotalCount(@PathVariable String id) {
        return ApiResponse.<Integer>builder()
                .result(clsService.getClassMemberCount(id))
                .build();
    }

    @GetMapping("/male/{id}")
    public ApiResponse<Integer> getMaleCount(@PathVariable String id) {
        return ApiResponse.<Integer>builder()
                .result(clsService.getClassMaleCount(id))
                .build();
    }

    @GetMapping("/female/{id}")
    public ApiResponse<Integer> getFemaleCount(@PathVariable String id) {
        return ApiResponse.<Integer>builder()
                .result(clsService.getClassFemaleCount(id))
                .build();
    }

    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ClassNameResponse> create(@Valid @RequestBody ClassnameCreateRequest request) {

        ClassName cls = clsService.createClass(request);
        return ApiResponse.<ClassNameResponse>builder()
                .result(clsMapper.toClassNameResponse(cls))
                .build();
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ClassNameResponse> update(
            @PathVariable String id, @Valid @RequestBody ClassnameUpdateRequest request) {

        ClassName cls = clsService.updateClass(id, request);
        return ApiResponse.<ClassNameResponse>builder()
                .result(clsMapper.toClassNameResponse(cls))
                .build();
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> delete(@PathVariable String id) {

        clsService.removeClassByID(id);
        return ApiResponse.<Void>builder().message("Delete class successful").build();
    }
}
