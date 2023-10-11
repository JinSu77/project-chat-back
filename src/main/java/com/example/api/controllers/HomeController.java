package com.example.api.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @RequestMapping(value = "/", produces = "application/json")
    public String index() {
        return "{\"message\": \"Hello World\"}";
    }
}
