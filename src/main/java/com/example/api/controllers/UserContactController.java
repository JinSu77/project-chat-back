package com.example.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.api.handlers.ResponseHandler;
import com.example.api.services.Contact.ContactService;
import com.example.api.services.User.UserService;

@Controller
@RequestMapping("/users/{userId}/contacts")
public class UserContactController {
    @Autowired 
    ContactService contactService;

    @Autowired
    UserService userService;

    @PostMapping("/{contactId}")
    public ResponseEntity<Object> store(
        @PathVariable("userId") Integer userId,
        @PathVariable("contactId") Integer contactId
    ) {
        try {
            userService.addContactList(userId, contactId);

            return ResponseHandler.generateResponse(HttpStatus.OK, "Contact added to your list");
        } catch (Exception e) {
            return ResponseHandler.generateResponse(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        }
    }
}
