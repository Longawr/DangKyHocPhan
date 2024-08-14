package com.dust.courseRegistration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dust.courseRegistration.entity.Role;
import com.dust.courseRegistration.enums.RoleType;

@Repository
public interface RoleRepository extends JpaRepository<Role, RoleType> {}
