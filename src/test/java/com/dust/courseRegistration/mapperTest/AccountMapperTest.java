package com.dust.courseRegistration.mapperTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.dust.courseRegistration.dto.response.AccountResponse;
import com.dust.courseRegistration.dto.response.RoleResponse;
import com.dust.courseRegistration.entity.Account;
import com.dust.courseRegistration.entity.Permission;
import com.dust.courseRegistration.entity.Role;
import com.dust.courseRegistration.enums.RoleType;
import com.dust.courseRegistration.mapper.AccountMapper;
import com.dust.courseRegistration.mapper.RoleMapper;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class AccountMapperTest {

    @Autowired
    private AccountMapper accMapper;

    @SuppressWarnings("unused")
    @Autowired
    private RoleMapper roleMapper;

    @Test
    public void testToAccountResponse() {
        // Create a mock Permission
        Permission permission = Permission.builder().name("READ_PRIVILEGES").build();

        // Create a mock Role
        Role role = Role.builder()
                .id(RoleType.ADMIN)
                .name("Admin Role")
                .permissions(Set.of(permission))
                .build();

        // Create a mock Account
        Account account = Account.builder()
                .username("testuser")
                .password("password")
                .roles(Set.of(role))
                .build();

        // Perform the mapping
        AccountResponse accountResponse = accMapper.toAccountResponse(account);
        log.info("Account: {}", account);
        log.info("AccountResponse: {}", accountResponse);

        // Assertions
        assertEquals(account.getUsername(), accountResponse.getUsername());
        assertEquals(1, accountResponse.getRoles().size());

        RoleResponse roleResponse = accountResponse.getRoles().get(0);
        assertEquals(role.getId().name(), roleResponse.getId());
        List<String> expectedPermissions =
                role.getPermissions().stream().map(Permission::getName).collect(Collectors.toList());
        assertEquals(expectedPermissions, roleResponse.getPermissions());
    }
}
