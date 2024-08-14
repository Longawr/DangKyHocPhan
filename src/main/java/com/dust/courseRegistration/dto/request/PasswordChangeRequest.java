package com.dust.courseRegistration.dto.request;

import jakarta.validation.constraints.NotBlank;

public record PasswordChangeRequest(
        @NotBlank(message = "this field is mandatory") String oldPassword,
        @NotBlank(message = "this field is mandatory") String newPassword) {}
