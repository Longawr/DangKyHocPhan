package com.dust.courseRegistration.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CourseStudentCreateRequest(
        @NotBlank(message = "this field is mandatory") int courseId,
        @NotBlank(message = "this field is mandatory") String studentId) {}
