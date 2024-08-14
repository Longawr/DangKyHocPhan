package com.dust.courseRegistration.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.dust.courseRegistration.dto.response.AccountResponse;
import com.dust.courseRegistration.entity.Account;

@Mapper(
        componentModel = "spring",
        uses = {RoleMapper.class})
public interface AccountMapper {

    @Mapping(target = "roles", source = "roles")
    AccountResponse toAccountResponse(Account account);
}
