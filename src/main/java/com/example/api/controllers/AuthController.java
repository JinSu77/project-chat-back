package com.example.api.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.api.handlers.ResponseHandler;
import com.example.api.services.User.UserService;
import com.example.api.validation.Users.UserDto;

import jakarta.validation.Valid;


@Controller
@RequestMapping("/auth")
public class AuthController {
    private UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/register")
    public @ResponseBody ResponseEntity<Object> register(@Valid @RequestBody UserDto userDto) {
        try {
            userService.saveUser(userDto);

            return ResponseHandler.generateResponse(HttpStatus.OK, "User registered successfully");
        } catch (Exception e) {
            return ResponseHandler.generateResponse(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        }
    }

    @PostMapping(value = "/login")
    public @ResponseBody ResponseEntity<Object> login() {
        return ResponseHandler.generateResponse(HttpStatus.OK, "login");
    }

    @PostMapping(value = "/logout")
    public @ResponseBody ResponseEntity<Object> logout() {
        return ResponseHandler.generateResponse(HttpStatus.OK, "logout");
    }

    @GetMapping(value = "/me")
    public @ResponseBody ResponseEntity<Object> me() {
        return ResponseHandler.generateResponse(HttpStatus.OK, "me");
    }
}
