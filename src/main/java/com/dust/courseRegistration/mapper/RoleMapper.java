package com.dust.courseRegistration.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.dust.courseRegistration.dto.request.RoleCreateRequest;
import com.dust.courseRegistration.dto.response.RoleResponse;
import com.dust.courseRegistration.entity.Permission;
import com.dust.courseRegistration.entity.Role;
import com.dust.courseRegistration.enums.RoleType;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(source = "id", target = "id", qualifiedByName = "stringToRoleType")
    @Mapping(source = "permissions", target = "permissions", qualifiedByName = "listStringToSetPermission")
    Role toRole(RoleCreateRequest request);

    @Mapping(target = "id", expression = "java(role.getId().name())")
    @Mapping(target = "permissions", qualifiedByName = "setPermissionToListString")
    RoleResponse toRoleResponse(Role role);

    // Convert String to RoleType
    @Named("stringToRoleType")
    static RoleType stringToRoleType(String id) {
        return id != null ? RoleType.valueOf(id.toUpperCase()) : null;
    }

    // Convert List<String> to Set<Permission>
    @Named("listStringToSetPermission")
    static Set<Permission> listStringToSetPermission(List<String> permissions) {
        if (permissions == null) {
            return Collections.emptySet();
        }
        return permissions.stream()
                .map(permission -> Permission.builder().name(permission).build())
                .collect(Collectors.toSet());
    }

    @Named("setPermissionToListString")
    static List<String> setPermissionToListString(Set<Permission> permissions) {
        if (permissions == null) {
            return Collections.emptyList();
        }
        return permissions.stream().map(Permission::getName).collect(Collectors.toList());
    }
}
