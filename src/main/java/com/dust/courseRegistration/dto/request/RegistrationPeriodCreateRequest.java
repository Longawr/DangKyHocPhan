package com.dust.courseRegistration.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;

public record RegistrationPeriodCreateRequest(
        @NotBlank(message = "this field is mandatory") int id,
        @NotBlank(message = "this field is mandatory") int semesterId,
        @NotBlank(message = "this field is mandatory") int year,
        LocalDate dateStart,
        LocalDate dateEnd) {}
