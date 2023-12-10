package com.example.api.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import com.example.api.handlers.ResponseHandler;
import com.example.api.models.User;
import com.example.api.repositories.UserRepository;
import com.example.api.services.User.UserService;

@Controller
@RequestMapping("/users/{userId}/contacts")
public class UserContactController {
    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/random")
    private ResponseEntity<Object> random(
        @PathVariable("userId") Integer userId
    ) {
        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND, null, "User not found");
        }

        List<User> contacts = userService.getRandomContactListFor(user.get());

        return ResponseHandler.generateResponse(HttpStatus.OK, "contacts", contacts);
    }

    @GetMapping
    public ResponseEntity<Object> index(
        @PathVariable("userId") Integer userId
    ) {
        try {
            List<User> contacts = userService.getContactList(userId);

            return ResponseHandler.generateResponse(HttpStatus.OK, "contacts", contacts);
        } catch (ResponseStatusException responseStatusException) {
            return ResponseHandler.generateResponse(responseStatusException, null, responseStatusException.getMessage());
        } catch (Exception e) {
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null, e.getMessage());
        }
    }

    @PostMapping("/{contactId}")
    public ResponseEntity<Object> store(
        @PathVariable("userId") Integer userId,
        @PathVariable("contactId") Integer contactId
    ) {
        try {
            List<User> contacts = userService.addContactList(userId, contactId);

            return ResponseHandler.generateResponse(HttpStatus.OK, "contacts", contacts);
        } catch (ResponseStatusException responseStatusException) {
            return ResponseHandler.generateResponse(responseStatusException, null, responseStatusException.getMessage());
        } catch (Exception e) {
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null, e.getMessage());
        }
    }
}
