package com.project.lovlev.services.security;

import org.springframework.security.core.Authentication;

public interface IAuthenticationFacade {
    Authentication getAuthentication();
    boolean returnRole(String role);
    Long getUserId();
    String getRolesString();
}
