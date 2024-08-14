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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dust.courseRegistration.dto.request.CourseCreateRequest;
import com.dust.courseRegistration.dto.request.CourseUpdateRequest;
import com.dust.courseRegistration.dto.response.ApiResponse;
import com.dust.courseRegistration.dto.response.CourseResponse;
import com.dust.courseRegistration.entity.Course;
import com.dust.courseRegistration.exception.AppException;
import com.dust.courseRegistration.exception.ErrorCode;
import com.dust.courseRegistration.mapper.CourseMapper;
import com.dust.courseRegistration.service.CourseService;
import com.dust.courseRegistration.util.RoleCheckUtils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/courses")
public class CourseController {

    CourseMapper crsMapper;

    CourseService crsService;

    @GetMapping
    public ApiResponse<List<CourseResponse>> getCourses() {

        List<Course> courses = crsService.getCourses();
        return ApiResponse.<List<CourseResponse>>builder()
                .result(courses.stream().map(crsMapper::toCourseResponse).toList())
                .build();
    }

    @GetMapping("/semester")
    public ApiResponse<List<CourseResponse>> getCoursesBySemester(
            @RequestParam int semesterId, @RequestParam int year) {

        List<Course> courses = crsService.getCoursesBySemester(semesterId, year);
        return ApiResponse.<List<CourseResponse>>builder()
                .result(courses.stream().map(crsMapper::toCourseResponse).toList())
                .build();
    }

    @GetMapping("/studentcount/{id}")
    public ApiResponse<Integer> getStudentCount(@PathVariable int id) {

        return ApiResponse.<Integer>builder()
                .result(crsService.getCourseMemberCount(id))
                .build();
    }

    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN') or #request.teacherId() == authentication.name")
    public ApiResponse<CourseResponse> create(@Valid @RequestBody CourseCreateRequest request) {

        Course crs = crsService.createCourse(request);
        return ApiResponse.<CourseResponse>builder()
                .result(crsMapper.toCourseResponse(crs))
                .build();
    }

    @PutMapping("/update/{courseId}")
    @PreAuthorize("hasRole('GV')")
    public ApiResponse<CourseResponse> update(
            @PathVariable int courseId,
            @Valid @RequestBody CourseUpdateRequest request,
            Authentication authentication) {

        boolean isAdmin = RoleCheckUtils.isAdmin(authentication);
        boolean isExists = crsService.checkCourseExistByTeacher(courseId, authentication.getName());
        log.info("isExists: {}", isExists);
        if (!(isAdmin || isExists)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        Course crs = crsService.updateCourse(courseId, request, isAdmin);
        return ApiResponse.<CourseResponse>builder()
                .result(crsMapper.toCourseResponse(crs))
                .build();
    }

    @DeleteMapping("/delete/{courseId}")
    @PreAuthorize("hasRole('GV')")
    public ApiResponse<Void> delete(@PathVariable int courseId, Authentication authentication) {

        if (!(RoleCheckUtils.isAdmin(authentication)
                || crsService.checkCourseExistByTeacher(courseId, authentication.getName()))) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        crsService.removeCourseByID(courseId);
        return ApiResponse.<Void>builder().message("Delete course successful").build();
    }
}
