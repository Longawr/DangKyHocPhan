package com.dust.courseRegistration.service;

import java.util.List;
import java.util.Set;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dust.courseRegistration.dto.request.AccountCreateRequest;
import com.dust.courseRegistration.dto.request.LoginRequest;
import com.dust.courseRegistration.dto.request.PasswordChangeRequest;
import com.dust.courseRegistration.entity.Account;
import com.dust.courseRegistration.entity.Role;
import com.dust.courseRegistration.enums.RoleType;
import com.dust.courseRegistration.exception.AppException;
import com.dust.courseRegistration.exception.ErrorCode;
import com.dust.courseRegistration.repository.AccountRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountService {

    PasswordEncoder pwdEncoder;

    AccountRepository accRepo;

    RoleService roleService;
    AuthenticationService authService;

    @Transactional
    public boolean existAccountByRole(RoleType role) {
        var rl = Role.builder().id(role).build();
        return accRepo.existsByRoles(Set.of(rl));
    }

    @Transactional
    public List<Account> getAllAccounts() {
        return accRepo.findAll();
    }

    @Transactional
    @Cacheable(value = "accounts", key = "#username")
    public Account getAccount(String username) {
        return accRepo.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }

    @Transactional
    public Account createAccount(AccountCreateRequest request) {
        String username = request.username();
        List<String> roleList = request.roles();

        if (accRepo.existsById(username)) throw new AppException(ErrorCode.USER_EXISTED);

        Set<Role> roles = !(roleList == null || roleList.isEmpty())
                ? roleService.getRolesFromRequest(roleList)
                : Set.of(new Role(RoleType.SV, null, null));

        var acc = Account.builder()
                .username(username)
                .password(pwdEncoder.encode(request.password()))
                .roles(roles)
                .build();

        return accRepo.save(acc);
    }

    @Transactional
    @CachePut(value = "accounts", key = "#username")
    public Account changePassword(String username, PasswordChangeRequest request) {
        Account acc = authService.authenticate(new LoginRequest(username, request.oldPassword()));
        acc.setPassword(pwdEncoder.encode(request.newPassword()));
        return accRepo.save(acc);
    }

    @Transactional
    @CachePut(value = "accounts", key = "#username")
    public Account updateRoles(String username, List<String> request) {
        Set<Role> roles = roleService.getRolesFromRequest(request);

        Account acc = accRepo.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        acc.setRoles(roles);
        return accRepo.save(acc);
    }

    @Transactional
    @CacheEvict(value = "accounts", key = "#username")
    public void deleteAccount(String username) {
        accRepo.deleteById(username);
    }
}
