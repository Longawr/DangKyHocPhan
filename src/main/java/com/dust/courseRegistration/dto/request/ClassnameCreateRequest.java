package com.dust.courseRegistration.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ClassnameCreateRequest(@NotBlank(message = "this field is mandatory") String id, String name) {}
