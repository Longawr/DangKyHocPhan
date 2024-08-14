package com.dust.courseRegistration.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dust.courseRegistration.entity.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {}
