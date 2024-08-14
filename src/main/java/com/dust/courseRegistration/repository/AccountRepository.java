package com.dust.courseRegistration.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dust.courseRegistration.entity.Account;
import com.dust.courseRegistration.entity.Role;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {

    boolean existsByUsername(String username);

    boolean existsByRoles(Set<Role> roles);

    @EntityGraph(attributePaths = {"roles", "roles.permissions"})
    Optional<Account> findByUsername(String username);
}
