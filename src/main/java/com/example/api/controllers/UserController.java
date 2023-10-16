package com.example.api.controllers;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.api.handlers.ResponseHandler;
import com.example.api.models.User;
import com.example.api.repositories.UserRepository;
import com.example.api.validation.Users.StoreUserDTO;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired

    private UserRepository userRepository;

    @GetMapping()
    public @ResponseBody ResponseEntity<Object> getAllUsers() {
        Iterable<User> users = userRepository.findAll();

        HashMap<String, Object> response = new HashMap<String, Object>();

        for (User user : users) {
            response.put(user.getId(), user.toArray());
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/register")
    public @ResponseBody ResponseEntity<Object> register(@Valid @RequestBody StoreUserDTO userPayload) {
        
            User user = userPayload.toUser();

            try {
                userRepository.save(user);

                return ResponseHandler.generateResponse(HttpStatus.CREATED, user.toArray());
            } catch (Exception e) {
                return ResponseHandler.generateResponse(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
            }
    }
}
