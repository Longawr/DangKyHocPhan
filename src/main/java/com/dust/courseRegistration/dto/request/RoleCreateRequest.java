package com.dust.courseRegistration.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotBlank;

public record RoleCreateRequest(
        @NotBlank(message = "this field is mandatory") String id, String name, List<String> permissions) {}
