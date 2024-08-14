package com.dust.courseRegistration.dto.request;

import jakarta.validation.constraints.NotBlank;

public record SubjectCreateRequest(
        @NotBlank(message = "this field is mandatory") String id, String name, int credits) {}
