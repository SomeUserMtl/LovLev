package com.project.lovlev.services;

import com.project.lovlev.models.SecurityUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacade implements IAuthenticationFacade {

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
                .noneMatch(s -> s.equals(role));
    }

    @Override
    public Long getUserId() {
        SecurityUser securityUser = (SecurityUser) getAuthentication().getPrincipal();
        return securityUser.getCurrentUserId();
    }

}
