package com.dust.courseRegistration.util;

import org.springframework.security.core.Authentication;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class RoleCheckUtils {

    /**
     * Check current user authorize if they are an administrator or not.
     *
     * @param	authentication
     * 			org.springframework.security.core.Authentication that contains username and authorities
     *
     * @return	{@code true}
     * 			if current user has "ROLE_ADMIN" authority
     */
    public static boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
    }

    /**
     * Check current user authorize if they are teacher or not.
     *
     * @param	authentication
     * 			org.springframework.security.core.Authentication that contains username and authorities
     *
     * @return	{@code true}
     * 			if current user has "ROLE_GV" authority
     */
    public static boolean isGv(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_GV"));
    }

    /**
     * Check current user authorize if they are not administrator and not teacher.
     *
     * @param	authentication
     * 			org.springframework.security.core.Authentication that contains username and authorities
     *
     * @return	{@code true}
     * 			if current user has "ROLE_SV" authority
     */
    public static boolean isSv(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_SV"));
    }
}
