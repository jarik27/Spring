package com.sda.springmvc.example.controllers;

import com.sda.springmvc.example.entities.User;
import com.sda.springmvc.example.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RestUserController {

    private final UserRepository userRepository;

    public RestUserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    public List<User> fetchAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> fetchUserById(@PathVariable long id) {
        return userRepository.findById(id)
                .map(this::ok)
                .orElseGet(this::userNotFound);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<User> deleteUserById(@PathVariable long id) {
        return userRepository.findById(id)
                .map(this::delete)
                .orElseGet(this::userNotFound);
    }

    private ResponseEntity<User> ok(User user) {
        return ResponseEntity.ok(user);
    }

    private ResponseEntity<User> delete(User user) {
        userRepository.delete(user);
        return ResponseEntity.ok(user);
    }

    private ResponseEntity<User> userNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
