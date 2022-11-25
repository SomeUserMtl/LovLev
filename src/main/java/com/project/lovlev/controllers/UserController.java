package com.project.lovlev.controllers;

import com.project.lovlev.configs.IAuthenticationFacade;
import com.project.lovlev.models.SecurityUser;
import com.project.lovlev.models.User;
import com.project.lovlev.repositories.UserRepository;
import com.project.lovlev.services.CustomValidators;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@Data
@RestController
@EnableMethodSecurity
public class UserController {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    CustomValidators customValidators;
    IAuthenticationFacade authentication;

    public UserController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          CustomValidators customValidators,
                          IAuthenticationFacade authentication) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.customValidators = customValidators;
        this.authentication = authentication;
    }

    // Create user
//    @PermitAll
    @PostMapping(path = "register",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> register(@RequestBody @Valid User newUser) throws Exception {

        // Ensure only users with ROLE_ADMIN can create users with ROLE_ADMIN, rest it's ROLE_USER
        if (authentication.getAuthentication().getPrincipal() == "anonymousUser")
            newUser.setRoles("ROLE_USER");
        else {
            SecurityUser userPrincipal = ((SecurityUser) authentication
                    .getAuthentication()
                    .getPrincipal());
            if (!userPrincipal.hasRole("ROLE_ADMIN")) {
                newUser.setRoles("ROLE_USER");
            }
        }

        customValidators.validatePassword(newUser.getPassword());
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        User user = userRepository.save(newUser);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }


//    @PreAuthorize("'ROLE_ANONYMOUS' != authentication.principal.authorities")
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
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @DeleteMapping("user")
    public ResponseEntity<User> delete(@RequestParam Long id) {
        userRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Update User
}
