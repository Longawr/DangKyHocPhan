package com.dust.courseRegistration.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dust.courseRegistration.dto.request.RoleCreateRequest;
import com.dust.courseRegistration.entity.Role;
import com.dust.courseRegistration.enums.RoleType;
import com.dust.courseRegistration.exception.AppException;
import com.dust.courseRegistration.exception.ErrorCode;
import com.dust.courseRegistration.mapper.RoleMapper;
import com.dust.courseRegistration.repository.RoleRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {

    RoleRepository roleRepo;
    RoleMapper roleMapper;

    @Transactional
    public Role createRole(RoleCreateRequest request) {
        Role role = roleMapper.toRole(request);

        if (roleRepo.existsById(role.getId())) throw new AppException(ErrorCode.ITEM_EXISTED);

        return roleRepo.save(role);
    }

    @Transactional
    @Cacheable(value = "roles", key = "#roleId.name()")
    public Role getRole(RoleType roleId) {
        return roleRepo.findById(roleId).orElseThrow(() -> new AppException(ErrorCode.ITEM_NOT_EXISTED));
    }

    @Transactional
    public Set<Role> getRolesFromRequest(List<String> request) {
        var roles = new HashSet<Role>();

        request.forEach(role -> {
            try {
                Role r = getRole(RoleType.valueOf(role));
                roles.add(r);
            } catch (AppException ex) {
            }
        });
        return roles;
    }
}
