package com.dust.courseRegistration.dto.request;

import jakarta.validation.constraints.NotBlank;

public record TeacherUpdateRequest(
        String firstName, String lastName, @NotBlank(message = "this field is mandatory") String sex) {}
