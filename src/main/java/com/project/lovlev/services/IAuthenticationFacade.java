package com.project.lovlev.services;

import org.springframework.security.core.Authentication;

public interface IAuthenticationFacade {
    Authentication getAuthentication();
    boolean returnRole(String role);
    Long getUserId();
}
