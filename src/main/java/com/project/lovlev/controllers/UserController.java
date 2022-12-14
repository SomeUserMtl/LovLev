package com.project.lovlev.controllers;

import com.project.lovlev.exceptions.NotFoundException;
import com.project.lovlev.services.security.IAuthenticationAccess;
import com.project.lovlev.models.User;
import com.project.lovlev.repositories.UserRepository;
import com.project.lovlev.services.validation.UserPost;
import com.project.lovlev.services.validation.UserPut;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.Arrays;
import java.util.Objects;

@Data
@RestController
public class UserController {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    IAuthenticationAccess authentication;

    public UserController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          IAuthenticationAccess authentication) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authentication = authentication;
    }

    // Get User by userId
    @GetMapping("/user/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {

        if(authentication.returnRole("ROLE_ADMIN")
                || Objects.equals(authentication.getUserId(), id)) {
            return userRepository.getById(id)
                    .map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    // Get all users
    @GetMapping("/users")
    public ResponseEntity<Iterable<User>> getAllUsers() {
        if (authentication.returnRole("ROLE_ADMIN")) {
            Iterable<User> users = userRepository.findAll();
            return new ResponseEntity<>(users, HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    // Create user
    @PostMapping(path = "/user",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> create(@RequestBody @Validated(UserPost.class) User newUser) {

        // only ROLE_ADMIN can create roles, default is ROLE_USER
        if(!authentication.returnRole("ROLE_ADMIN")) {
            if (newUser.getRoles() != null)
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            newUser.setRoles("ROLE_USER");
        }

        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        User user = userRepository.save(newUser);

        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    //delete user
    @DeleteMapping(path = "/user/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable Long id) {

        // Evaluate if user has permission for this action
        if(Objects.equals(authentication.getUserId(), id)
                || authentication.returnRole("ROLE_ADMIN")) {
            userRepository.deleteUserById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    // Delete multiple users
    @DeleteMapping("/users")
    public ResponseEntity<Iterable<User>> deleteUsers(@RequestParam Long[] ids) {
        if (authentication.returnRole("ROLE_ADMIN")){
            userRepository.deleteAllByIdIn(Arrays.asList(ids));
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    //update user
    @PutMapping(path = "/user/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody @Validated(UserPut.class) User updatedUser){

        if (authentication.returnRole("ROLE_ADMIN")
                || Objects.equals(authentication.getUserId(), id)) {

            User user = userRepository.getById(id).orElseThrow(()
                    -> new NotFoundException("User not found with id " + id));

            if(updatedUser.getRoles() != null) {
                if (authentication.returnRole("ROLE_ADMIN"))
                    user.setRoles(updatedUser.getRoles());
                else
                    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            if (updatedUser.getFirstName() != null)
                user.setFirstName(updatedUser.getFirstName());
            if (updatedUser.getMiddleName() != null)
                user.setMiddleName(updatedUser.getMiddleName());
            if (updatedUser.getLastName() != null)
                user.setLastName(updatedUser.getLastName());
            if (updatedUser.getEmail() != null)
                user.setEmail(updatedUser.getEmail());
            if (updatedUser.getPassword() != null)
                user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            if (updatedUser.getUsername() != null)
                user.setUsername(updatedUser.getUsername());
            if (updatedUser.getSex() != null)
                user.setSex(updatedUser.getSex());

            userRepository.save(user);
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
