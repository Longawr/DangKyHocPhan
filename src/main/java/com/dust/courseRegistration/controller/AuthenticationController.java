package com.dust.courseRegistration.controller;

import java.text.ParseException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dust.courseRegistration.dto.request.LoginRequest;
import com.dust.courseRegistration.dto.response.ApiResponse;
import com.dust.courseRegistration.dto.response.LoginResponse;
import com.dust.courseRegistration.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

    AuthenticationService authenticationService;

    @PostMapping("/login")
    ApiResponse<LoginResponse> login(@RequestBody LoginRequest request) {

        LoginResponse result = authenticationService.login(request);
        return ApiResponse.<LoginResponse>builder().result(result).build();
    }

    @PostMapping("/introspect")
    ApiResponse<Boolean> checkToken(@RequestBody String request) throws ParseException, JOSEException {

        Boolean result = authenticationService.introspect(request);
        return ApiResponse.<Boolean>builder().result(result).build();
    }

    @PostMapping("/refresh")
    ApiResponse<LoginResponse> refreshToken(@RequestBody String refreshToken) throws ParseException, JOSEException {

        LoginResponse result = authenticationService.refreshToken(refreshToken);
        return ApiResponse.<LoginResponse>builder().result(result).build();
    }

    @PostMapping("/logout")
    ApiResponse<Void> logout(@RequestBody String token) throws ParseException, JOSEException {

        authenticationService.logout(token);
        return ApiResponse.<Void>builder().message("Logout Successful").build();
    }
}
