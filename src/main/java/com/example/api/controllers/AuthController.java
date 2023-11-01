package com.example.api.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.api.handlers.ResponseHandler;
import com.example.api.models.User;
import com.example.api.repositories.UserRepository;
import com.example.api.services.Auth.JwtUtil;
import com.example.api.services.User.UserService;
import com.example.api.validation.Auth.UserLoginDTO;
import com.example.api.validation.Users.UserDto;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;


@Controller
@RequestMapping("/auth")
public class AuthController {
    private JwtUtil jwtUtil;
    private UserRepository userRepository;
    private UserService userService;

    public AuthController(JwtUtil jwtUtil, UserRepository userRepository, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @PostMapping(value = "/register")
    public @ResponseBody ResponseEntity<Object> register(@Valid @RequestBody UserDto userDto) {
        try {
            userService.saveUser(userDto);

            User user = userRepository.findByUsername(userDto.getUsername());

            Map<String, Object> response = new HashMap<String, Object>();

            response.put("user", user);

            return ResponseHandler.generateResponse(HttpStatus.OK, response);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        }
    }

    @PostMapping(value = "/login")
    public @ResponseBody ResponseEntity<Object> login(@Valid @RequestBody UserLoginDTO userLoginDTO) {
        try {
            User user = userService.login(userLoginDTO);

            String token = jwtUtil.createToken(user);

            Map<String, Object> response = new HashMap<String, Object>();

            response.put("token", token);
            response.put("message", "logged in successfully");

            return ResponseHandler.generateResponse(HttpStatus.OK, response);
        } catch (BadCredentialsException e){
            return ResponseHandler.generateResponse(HttpStatus.UNPROCESSABLE_ENTITY, "Wrong credentials");
        } catch (Exception e) {
            return ResponseHandler.generateResponse(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        }
    }

    @GetMapping(value = "/me")
    public @ResponseBody ResponseEntity<Object> me(@Nullable @RequestHeader("Authorization") String authorization) {
        if (authorization == null) {
            return ResponseHandler.generateResponse(HttpStatus.UNPROCESSABLE_ENTITY, "Authorization header is missing");
        }

        String token = authorization.replace("Bearer ", "");
        String username = jwtUtil.getAuthUsername(token);

        if (username == null) {
            return ResponseHandler.generateResponse(HttpStatus.UNPROCESSABLE_ENTITY, "Invalid token");
        }

        User user = userRepository.findByUsername(username);

        if (user == null) {
            return ResponseHandler.generateResponse(HttpStatus.UNPROCESSABLE_ENTITY, "User not found");
        }

        Map<String, Object> response = new HashMap<String, Object>();

        response.put("user", user);

        return ResponseHandler.generateResponse(HttpStatus.OK, response);
    }
}
