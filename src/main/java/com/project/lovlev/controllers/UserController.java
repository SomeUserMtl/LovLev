package com.project.lovlev.controllers;

import com.project.lovlev.services.IAuthenticationFacade;
import com.project.lovlev.models.User;
import com.project.lovlev.repositories.UserRepository;
import com.project.lovlev.services.CustomValidators;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.Objects;

@Data
@RestController
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
    @PostMapping(path = "/user/register",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> register(@RequestBody @Valid User newUser) {

        // only ROLE_ADMIN can create roles, default is ROLE_USER
        if(authentication.returnRole("ROLE_ADMIN"))
            newUser.setRoles("ROLE_USER");

        // validate password before encoding
        customValidators.validatePassword(newUser.getPassword());
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        User user = userRepository.save(newUser);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    //delete user
    @DeleteMapping(path = "/user/delete")
    public ResponseEntity<User> deleteUser(@RequestParam Long id) {

        // Evaluate if user has permission for this action
        if(!Objects.equals(authentication.getUserId(), id)
                && authentication.returnRole("ROLE_ADMIN"))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        userRepository.deleteUserById(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
