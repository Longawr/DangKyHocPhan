package com.dust.courseRegistration.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotBlank;

public record AccountCreateRequest(
        @NotBlank(message = "this field is mandatory") String username,
        @NotBlank(message = "this field is mandatory") String password,
        List<String> roles) {}
