package com.dust.courseRegistration.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;

public record StudentCreateRequest(
        @NotBlank(message = "this field is mandatory") String username,
        String firstName,
        String lastName,
        @NotBlank(message = "this field is mandatory") String sex,
        LocalDate dob,
        String classNameId) {}
