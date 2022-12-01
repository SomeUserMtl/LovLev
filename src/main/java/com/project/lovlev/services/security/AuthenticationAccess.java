package com.project.lovlev.services.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationAccess implements IAuthenticationAccess {

    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public boolean returnRole(String role) {
        return getAuthentication()
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(s -> s.equals(role));
    }

    @Override
    public String getRolesString() {
        return getAuthentication()
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .reduce(",", (a, b) -> a + b);
    }

    @Override
    public Long getUserId() {
        SecurityUser securityUser = (SecurityUser) getAuthentication().getPrincipal();
        return securityUser.getCurrentUserId();
    }

}
