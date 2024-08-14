package com.dust.courseRegistration.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "this field is mandatory") String username,
        @NotBlank(message = "this field is mandatory") String password) {}
