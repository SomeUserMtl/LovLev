package com.project.lovlev.controllers;

import com.project.lovlev.models.User;
import com.project.lovlev.repositories.UserRepository;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Data
@RestController
@EnableMethodSecurity
public class AdminController {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    public AdminController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Get user by id
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/user")
    public ResponseEntity<User> getById(@RequestParam Long id) {
        Optional<User> user = Optional
                .ofNullable(userRepository.getById(id));
        return user.map(value
                -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(()
                -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Get all users
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/users")
    public ResponseEntity<Iterable<User>> getAllUsers() {
        Iterable<User> users = userRepository.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    //delete multiple users
    //not active
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/admin/users")
    public ResponseEntity<User> deleteAll(@RequestParam Long[] id) {
        userRepository.deleteAllByIdIn(List.of(id));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
