package com.dust.courseRegistration.service;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.dust.courseRegistration.dto.request.LoginRequest;
import com.dust.courseRegistration.dto.response.LoginResponse;
import com.dust.courseRegistration.entity.Account;
import com.dust.courseRegistration.entity.RefreshToken;
import com.dust.courseRegistration.exception.AppException;
import com.dust.courseRegistration.exception.ErrorCode;
import com.dust.courseRegistration.repository.AccountRepository;
import com.dust.courseRegistration.repository.RefreshTokenRepository;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationService {

    @Autowired
    @Lazy
    PasswordEncoder passwordEncoder;

    @Autowired
    AccountRepository accRepo;

    @Autowired
    RefreshTokenRepository rfsTokenRepo;

    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    @Value("${jwt.access-duration}")
    protected long ACCESS_DURATION;

    @Value("${jwt.refresh-duration}")
    protected long REFRESH_DURATION;

    final String ACCESS_TOKEN_ISSUER = "ACCESS_TOKEN";
    final String REFRESH_TOKEN_ISSUER = "REFRESH_TOKEN";

    public LoginResponse login(LoginRequest request) {
        Account acc = authenticate(request);
        return generateToken(acc);
    }

    public boolean introspect(String token) throws JOSEException, ParseException {
        boolean isValid = true;

        try {
            verifyToken(token);
        } catch (AppException e) {
            isValid = false;
        }

        return isValid;
    }

    @Transactional
    public LoginResponse refreshToken(String request) throws ParseException, JOSEException {
        SignedJWT signedJWT = verifyToken(request);
        String jit = signedJWT.getJWTClaimsSet().getJWTID();
        String username = signedJWT.getJWTClaimsSet().getSubject();
        String issuer = signedJWT.getJWTClaimsSet().getIssuer();

        if (!issuer.equals(REFRESH_TOKEN_ISSUER)) throw new AppException(ErrorCode.UNAUTHORIZED);

        RefreshToken refreshToken =
                rfsTokenRepo.findById(jit).orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        if (refreshToken.getExpiredAt().before(new Date())) {
            throw new AppException(ErrorCode.TOKEN_EXPIRED);
        }

        rfsTokenRepo.delete(refreshToken);

        Account acc = accRepo.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return generateToken(acc);
    }

    @Transactional
    public void logout(String token) throws ParseException, JOSEException {
        try {
            SignedJWT signToken = verifyToken(token);
            String jit = signToken.getJWTClaimsSet().getJWTID();

            rfsTokenRepo.deleteById(jit);
        } catch (AppException exception) {
            log.info("Token already expired");
        }
    }

    @Transactional
    public Account authenticate(LoginRequest request) {
        Account account = accRepo.findByUsername(request.username())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        boolean isMatched = passwordEncoder.matches(request.password(), account.getPassword());

        if (!isMatched) throw new AppException(ErrorCode.INCORRECT_PASSWORD);
        return account;
    }

    @Transactional
    private LoginResponse generateToken(Account acc) {
        String jwtID = UUID.randomUUID().toString();
        var issueAt = new Date();
        var header = new JWSHeader(JWSAlgorithm.HS512);

        var accessExpiredTime = Date.from(issueAt.toInstant().plus(ACCESS_DURATION, ChronoUnit.SECONDS));
        var accessTokenClaimsSet = new JWTClaimsSet.Builder()
                .subject(acc.getUsername())
                .issuer(ACCESS_TOKEN_ISSUER)
                .issueTime(issueAt)
                .expirationTime(accessExpiredTime)
                .jwtID(jwtID)
                .claim("scope", buildScope(acc))
                .build();
        var accessPayload = new Payload(accessTokenClaimsSet.toJSONObject());
        var accessTokenObject = new JWSObject(header, accessPayload);

        var refreshExpiredTime = Date.from(issueAt.toInstant().plus(REFRESH_DURATION, ChronoUnit.SECONDS));
        var refreshTokenClaimsSet = new JWTClaimsSet.Builder()
                .subject(acc.getUsername())
                .issuer(REFRESH_TOKEN_ISSUER)
                .issueTime(issueAt)
                .expirationTime(refreshExpiredTime)
                .jwtID(jwtID)
                .build();
        var refreshPayload = new Payload(refreshTokenClaimsSet.toJSONObject());
        var refreshTokenObject = new JWSObject(header, refreshPayload);

        try {
            accessTokenObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            refreshTokenObject.sign(new MACSigner(SIGNER_KEY.getBytes()));

            var rfshToken = new RefreshToken(jwtID, refreshExpiredTime);
            rfsTokenRepo.save(rfshToken);

            return LoginResponse.builder()
                    .accessToken(accessTokenObject.serialize())
                    .accessExpired(LocalDateTime.ofInstant(accessExpiredTime.toInstant(), ZoneId.systemDefault()))
                    .refreshToken(refreshTokenObject.serialize())
                    .build();
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new RuntimeException(e);
        }
    }

    @Transactional
    private SignedJWT verifyToken(String token) throws JOSEException, ParseException {
        var verifier = new MACVerifier(SIGNER_KEY.getBytes());

        var signedJWT = SignedJWT.parse(token);

        boolean isVerified = signedJWT.verify(verifier);
        if (!isVerified) throw new AppException(ErrorCode.UNAUTHENTICATED);

        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        if (expiryTime.before(new Date())) throw new AppException(ErrorCode.TOKEN_EXPIRED);

        return signedJWT;
    }

    private String buildScope(Account acc) {
        var stringJoiner = new StringJoiner(" ");

        acc.getRoles().forEach(role -> {
            stringJoiner.add("ROLE_" + role.getId().name());
            if (!CollectionUtils.isEmpty(role.getPermissions()))
                role.getPermissions().forEach(permission -> stringJoiner.add(permission.getName()));
        });

        return stringJoiner.toString();
    }
}
