package com.project.lovlev.services;

import org.springframework.security.core.Authentication;

import java.util.List;

public interface IAuthenticationFacade {
    Authentication getAuthentication();
    List<String> returnRoles();
}
