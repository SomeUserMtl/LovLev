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
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

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

    // Get user by id
    @GetMapping("/user")
    public ResponseEntity<User> getById(@RequestParam Long id) {
        Optional<User> user = Optional
                .ofNullable(userRepository.getById(id));
        return user.map(value
                -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(()
                -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Get all users
    @GetMapping("/users")
    public ResponseEntity<Iterable<User>> getAllUsers() {
        System.out.println("authentication: " + authentication.getUserId());
        Iterable<User> users = userRepository.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    // Create user
    @PostMapping(path = "/user",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> register(@RequestBody @Valid User newUser) {

        // only ROLE_ADMIN can create roles and IDs, default is ROLE_USER
        if(authentication.returnRole("ROLE_ADMIN") || newUser.getRoles() == null) {
            newUser.setRoles("USER");
            newUser.setId(null);
        }

        // validate password before encoding
        customValidators.validatePassword(newUser.getPassword());
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        User user = userRepository.save(newUser);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    //update user
    @PutMapping(path = "/user",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> updateUser(@RequestBody @Valid User updatedUser){

        // only ROLE_ADMIN can modify roles
        if(authentication.returnRole("ROLE_ADMIN")) {
            updatedUser.setRoles("ROLE_USER"); // test
        }

        updatedUser.setId(authentication.getUserId());
        userRepository.save(updatedUser);
        return new ResponseEntity<>(updatedUser, HttpStatus.CREATED);
    }

    //delete user
    @DeleteMapping(path = "/user")
    public ResponseEntity<User> deleteUser(@RequestParam Long id) {

        // Evaluate if user has permission for this action
        if(!Objects.equals(authentication.getUserId(), id)
                && authentication.returnRole("ROLE_ADMIN"))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        userRepository.deleteUserById(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    //delete multiple users
    @DeleteMapping("/users")
    public ResponseEntity<Iterable<User>> deleteUsers(@RequestParam Long[] ids) {
        userRepository.deleteAllByIdIn(Arrays.asList(ids));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
