package com.example.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.api.models.User;
import com.example.api.repositories.UserRepository;


@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired

    private UserRepository userRepository;

    @GetMapping()
    public @ResponseBody ResponseEntity<Object> getAllUsers() {
        Iterable<User> users = userRepository.findAll();

        return ResponseEntity.ok(users);
    }
}
