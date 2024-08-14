package com.dust.courseRegistration.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dust.courseRegistration.dto.request.CourseStudentCreateRequest;
import com.dust.courseRegistration.dto.response.ApiResponse;
import com.dust.courseRegistration.dto.response.CourseStudentResponse;
import com.dust.courseRegistration.entity.CourseStudent;
import com.dust.courseRegistration.exception.AppException;
import com.dust.courseRegistration.exception.ErrorCode;
import com.dust.courseRegistration.mapper.CourseStudentMapper;
import com.dust.courseRegistration.service.CourseStudentService;
import com.dust.courseRegistration.util.RoleCheckUtils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/coursestudents")
public class CourseStudentController {

    CourseStudentMapper csMapper;

    CourseStudentService csService;

    @GetMapping("/studentId/{studentId}")
    @PreAuthorize("hasRole('ADMIN') or #studentId == authentication.name")
    public ApiResponse<List<CourseStudentResponse>> getCourseByStudent(
            @PathVariable String studentId, @RequestParam int semesterId, @RequestParam int semesterYear) {

        List<CourseStudent> csList = csService.getCoursesByStudent(studentId, semesterId, semesterYear);
        return ApiResponse.<List<CourseStudentResponse>>builder()
                .result(csList.stream().map(csMapper::toCourseStudentResponse).toList())
                .build();
    }

    @GetMapping("/courseid/{courseId}")
    public ApiResponse<List<CourseStudentResponse>> getStudentsByCourse(@PathVariable int courseId) {

        List<CourseStudent> csList = csService.getStudentsByCourse(courseId);
        return ApiResponse.<List<CourseStudentResponse>>builder()
                .result(csList.stream().map(csMapper::toCourseStudentResponse).toList())
                .build();
    }

    @GetMapping("/countbysemester/{studentId}")
    @PreAuthorize("hasRole('SV') or #studentId == authentication.name")
    public ApiResponse<Integer> getCoursesCountByStudent(
            @PathVariable String studentId, @RequestParam int semesterId, @RequestParam int semesterYear) {

        return ApiResponse.<Integer>builder()
                .result(csService.getCoursesCountByStudent(studentId, semesterId, semesterYear))
                .build();
    }

    @GetMapping("/countall/studentid/{studentId}")
    @PreAuthorize("hasRole('SV') or #studentId == authentication.name")
    public ApiResponse<Integer> getCoursesCountByStudent(@PathVariable String studentId) {

        return ApiResponse.<Integer>builder()
                .result(csService.getCoursesCountByStudent(studentId))
                .build();
    }

    @GetMapping("/id")
    public ApiResponse<CourseStudentResponse> getCourseStudent(
            @RequestParam String studentId, @RequestParam int courseId) {

        CourseStudent cs = csService.getCourseStudent(studentId, courseId);
        return ApiResponse.<CourseStudentResponse>builder()
                .result(csMapper.toCourseStudentResponse(cs))
                .build();
    }

    @PostMapping("/save")
    @PreAuthorize("hasRole('SV')")
    public ApiResponse<CourseStudentResponse> enrollCourse(
            @RequestBody CourseStudentCreateRequest request, Authentication authentication) {

        String username = authentication.getName();

        if (!(RoleCheckUtils.isAdmin(authentication) || request.studentId().equalsIgnoreCase(username))) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        CourseStudent cs = csService.createCourseStudent(request);
        return ApiResponse.<CourseStudentResponse>builder()
                .result(csMapper.toCourseStudentResponse(cs))
                .build();
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('SV')")
    public ApiResponse<Void> leaveCourse(
            @RequestParam String studentId, @RequestParam int courseId, Authentication authentication) {

        String username = authentication.getName();
        boolean isAdmin = RoleCheckUtils.isAdmin(authentication);
        // Administrators can delete, and students can leave the enrolled course
        if (!isAdmin && !studentId.equalsIgnoreCase(username)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        csService.removeCourseStudent(courseId, studentId, isAdmin);
        return ApiResponse.<Void>builder().message("Deleted successful").build();
    }
}
