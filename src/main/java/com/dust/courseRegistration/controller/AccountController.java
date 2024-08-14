package com.dust.courseRegistration.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dust.courseRegistration.dto.request.AccountCreateRequest;
import com.dust.courseRegistration.dto.request.PasswordChangeRequest;
import com.dust.courseRegistration.dto.response.AccountResponse;
import com.dust.courseRegistration.dto.response.ApiResponse;
import com.dust.courseRegistration.entity.Account;
import com.dust.courseRegistration.mapper.AccountMapper;
import com.dust.courseRegistration.service.AccountService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/accounts")
public class AccountController {

    AccountMapper accMapper;

    AccountService accService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<List<AccountResponse>> getAllAccounts(Authentication authentication) {

        List<Account> acc = accService.getAllAccounts();
        return ApiResponse.<List<AccountResponse>>builder()
                .result(acc.stream().map(accMapper::toAccountResponse).toList())
                .build();
    }

    @GetMapping("/id/{username}")
    @PreAuthorize("hasRole('ADMIN') or #username == authentication.name")
    ApiResponse<AccountResponse> getAccountByUsername(@PathVariable String username) {

        Account acc = accService.getAccount(username);
        return ApiResponse.<AccountResponse>builder()
                .result(accMapper.toAccountResponse(acc))
                .build();
    }

    @GetMapping("/myinfo")
    ApiResponse<AccountResponse> getMyInfo(Authentication authentication) {

        Account acc = accService.getAccount(authentication.getName());
        return ApiResponse.<AccountResponse>builder()
                .result(accMapper.toAccountResponse(acc))
                .build();
    }

    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<AccountResponse> createAccount(@Valid @RequestBody AccountCreateRequest request) {

        Account acc = accService.createAccount(request);
        return ApiResponse.<AccountResponse>builder()
                .message("Create account successful")
                .result(accMapper.toAccountResponse(acc))
                .build();
    }

    @PutMapping("/update/password/{username}")
    @PreAuthorize("hasRole('ADMIN') or #username == authentication.name")
    ApiResponse<AccountResponse> changePassword(
            @PathVariable String username, @Valid @RequestBody PasswordChangeRequest request) {

        Account acc = accService.changePassword(username, request);
        return ApiResponse.<AccountResponse>builder()
                .message("Change password successful")
                .result(accMapper.toAccountResponse(acc))
                .build();
    }

    @PutMapping("/update/roles/{username}")
    @PostAuthorize("hasRole('ADMIN')")
    ApiResponse<AccountResponse> updateRoles(@PathVariable String username, @RequestBody List<String> request) {

        Account acc = accService.updateRoles(username, request);
        return ApiResponse.<AccountResponse>builder()
                .message("Change roles successful")
                .result(accMapper.toAccountResponse(acc))
                .build();
    }

    @DeleteMapping("/delete/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<Void> deleteAccount(@PathVariable String username) {

        accService.deleteAccount(username);
        return ApiResponse.<Void>builder().message("Delete account successful").build();
    }
}
