package com.example.api.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.api.handlers.ResponseHandler;
import com.example.api.models.User;
import com.example.api.repositories.UserRepository;


@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping()
    public @ResponseBody ResponseEntity<Object> index() {
        Iterable<User> users = userRepository.findAll();

        Map<String, Object> response = new HashMap<>();

        response.put("users", users);

        return ResponseHandler.generateResponse(HttpStatus.OK, response);
    }

    @GetMapping("/{userId}")
    public @ResponseBody ResponseEntity<Object> show(@PathVariable Integer userId) {
        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND, null);
        }

        Map<String, Object> response = new HashMap<>();

        response.put("user", user);

        return ResponseHandler.generateResponse(HttpStatus.OK, response);
    }
}
