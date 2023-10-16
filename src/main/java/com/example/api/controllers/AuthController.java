package com.example.api.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.api.handlers.ResponseHandler;

@Controller
@RequestMapping("/auth")
public class AuthController {

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
