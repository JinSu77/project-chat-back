package com.example.api.controllers;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.api.handlers.ResponseHandler;

@RestController
public class HomeController {
    @RequestMapping(value = "/", produces = "application/json")
    public ResponseEntity<Object> index() {
        return ResponseHandler.generateResponse(HttpStatus.OK, "api", Map.of(
            "name", "Spring Boot",
            "version", "3.1.4",
            "java", "21",
            "maven", "3.9.5"
        ));
    }
}
