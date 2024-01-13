package com.example.api.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.server.ResponseStatusException;

import com.example.api.dtos.Auth.UserLoginDTO;
import com.example.api.dtos.Users.UserDto;
import com.example.api.handlers.ResponseHandler;
import com.example.api.models.User;
import com.example.api.repositories.UserRepository;
import com.example.api.services.Auth.JwtUtil;
import com.example.api.services.User.UserService;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;


@Controller
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private JwtUtil jwtUtil;

    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping(value = "/register")
    public @ResponseBody ResponseEntity<Object> register(@Valid @RequestBody UserDto userDto) {
        try {
            userService.save(userDto);

            User user = userRepository.findByUsername(userDto.getUsername());

            return ResponseHandler.generateResponse(HttpStatus.OK, "user", user);
        } catch(ResponseStatusException responseStatusException) {
            return ResponseHandler.generateResponse(responseStatusException, null, responseStatusException.getMessage());
        } catch (Exception e) {
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null, e.getMessage());
        }
    }

    @PostMapping(value = "/login")
    public @ResponseBody ResponseEntity<Object> login(@Valid @RequestBody UserLoginDTO userLoginDTO) {
        try {
            User user = userService.login(userLoginDTO);

            Map<String, Object> response = Map.of(
                "message", "logged in successfully",
                "token", jwtUtil.createToken(user),
                "user", user,
                "mercureToken", jwtUtil.createMercureToken(user)
            );

            return ResponseHandler.generateResponse(HttpStatus.OK, null, response);
        } catch(ResponseStatusException responseStatusException) {
            return ResponseHandler.generateResponse(responseStatusException, null, responseStatusException.getMessage());
        } catch (BadCredentialsException e){
            return ResponseHandler.generateResponse(HttpStatus.UNPROCESSABLE_ENTITY, null, "Wrong credentials");
        } catch (Exception e) {
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null, e.getMessage());
        }
    }

    @GetMapping(value = "/me")
    public @ResponseBody ResponseEntity<Object> me(@Nullable @RequestHeader("Authorization") String authorization) {
        String token = authorization.replace("Bearer ", "");

        try {
            User user = jwtUtil.getAuthUser(token);

            return ResponseHandler.generateResponse(HttpStatus.OK, "user", user);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null, e.getMessage());
        }
    }
}
