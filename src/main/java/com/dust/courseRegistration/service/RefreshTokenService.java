package com.dust.courseRegistration.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;

import com.dust.courseRegistration.entity.RefreshToken;
import com.dust.courseRegistration.exception.AppException;
import com.dust.courseRegistration.exception.ErrorCode;
import com.dust.courseRegistration.repository.RefreshTokenRepository;

public class RefreshTokenService {
    RefreshTokenRepository rfsTokenRepo;

    @Transactional
    @Cacheable(value = "refreshTokens", key = "#jit")
    public RefreshToken getRefreshToken(String jit) {
        return rfsTokenRepo.findById(jit).orElseThrow(() -> new AppException(ErrorCode.ITEM_NOT_EXISTED));
    }

    @Transactional
    public boolean existsRefeshToken(String jit) {
        return rfsTokenRepo.existsById(jit);
    }

    @Transactional
    @CacheEvict(value = "refreshTokens", key = "#jit")
    public void deleteRefreshToken(String jit) {
        rfsTokenRepo.deleteById(jit);
    }

    @Transactional
    public RefreshToken createRefreshToken(RefreshToken rfshToken) {
        if (!existsRefeshToken(rfshToken.getId())) throw new AppException(ErrorCode.ITEM_EXISTED);
        return rfsTokenRepo.save(rfshToken);
    }
}
