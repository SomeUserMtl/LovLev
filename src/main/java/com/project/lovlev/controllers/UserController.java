package com.project.lovlev.controllers;

import com.project.lovlev.models.User;
import com.project.lovlev.repositories.UserRepository;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@Data
@RestController
public class UserController {
    UserRepository userRepository;
//    PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
//        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("user")
    public ResponseEntity<User> getById(@RequestParam String username,
                                        @RequestParam String password) {
        Optional<User> user = Optional.ofNullable(userRepository.findByUsernameAndPassword(username,password));
        return user.map(value
                -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(()
                -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    //findall users
    @GetMapping("users")
    public ResponseEntity<Iterable<User>> getAllUsers() {
        Iterable<User> users = userRepository.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    // Create user
    @PostMapping(path = "user",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> save(@RequestBody User newUser) throws Exception {
//        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        User user = userRepository.save(newUser);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    //delete user
    @DeleteMapping("user")
    public ResponseEntity<User> delete(@RequestParam Long id) {
        userRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //delete multiple users
    @DeleteMapping("users")
    public ResponseEntity<User> deleteAll(@RequestParam Long[] id) {
        userRepository.deleteAllByIdIn(List.of(id));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
