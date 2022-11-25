package com.project.lovlev.controllers;

import com.project.lovlev.models.SecurityUser;
import com.project.lovlev.models.User;
import com.project.lovlev.repositories.UserRepository;
import com.project.lovlev.services.CustomValidators;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;

@Data
@RestController
@EnableMethodSecurity
public class UserController {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    CustomValidators customValidators;

    public UserController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          CustomValidators customValidators) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.customValidators = customValidators;
    }

    // Create user
    @PermitAll
    @PostMapping(path = "register",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> register(@RequestBody @Valid User newUser,
                                         Authentication authentication) throws Exception {

        SecurityUser userPrincipal = (SecurityUser) authentication.getPrincipal();

        // ugly solution to disallow non-admins from adding roles
        if (newUser.getRoles() != null && userPrincipal
                .getAuthorities()
                .stream()
                .map(Object::toString)
                .noneMatch(s -> s.equals("ROLE_ADMIN"))) {
            throw new IllegalArgumentException("You are not authorized to add roles");
        }

        customValidators.validatePassword(newUser.getPassword());

        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        User user = userRepository.save(newUser);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }


//    @PreAuthorize("anonymous")
//    @PostMapping(path = "login",
//            consumes = MediaType.APPLICATION_JSON_VALUE,
//            produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<User> login(@RequestBody @Valid User newUser,
//                                         Authentication authentication) throws Exception {
//
//        SecurityUser userPrincipal = (SecurityUser) authentication.getPrincipal();
//
//        // ugly solution to disallow non-admins from adding roles
//        if (newUser.getRoles() != null && userPrincipal
//                .getAuthorities()
//                .stream()
//                .map(Object::toString)
//                .noneMatch(s -> s.equals("ROLE_ADMIN"))) {
//            throw new IllegalArgumentException("You are not authorized to add roles");
//        }
//
//        customValidators.validatePassword(newUser.getPassword());
//
//        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
//        User user = userRepository.save(newUser);
//        return new ResponseEntity<>(user, HttpStatus.CREATED);
//}


        // Delete user
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @DeleteMapping("user")
    public ResponseEntity<User> delete(@RequestParam Long id) {
        userRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Update User
}
