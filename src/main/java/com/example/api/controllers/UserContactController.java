package com.example.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.api.handlers.ResponseHandler;
import com.example.api.models.Contact;
import com.example.api.services.Contact.ContactService;
import com.example.api.services.User.UserService;

@Controller
@RequestMapping("/users/{userId}/contacts")
public class UserContactController {
    @Autowired 
    ContactService contactService;

    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity<Object> index(
        @PathVariable("userId") Integer userId
    ) {
        try {
            ResponseEntity<Object> contact = userService.getContactList(userId);

            return ResponseHandler.generateResponse(HttpStatus.OK, "contacts", contact);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null, e.getMessage());
        }
    }

    @GetMapping("/{contactId}")
    public ResponseEntity<Object> show(
        @PathVariable("userId") Integer userId,
        @PathVariable("contactId") Integer contactId
    ) {
        try {
            Contact contact = contactService.getContactById(contactId);

            return ResponseHandler.generateResponse(HttpStatus.OK, "contact", contact);
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
            userService.addContactList(userId, contactId);

            Contact contact = contactService.getContactById(contactId);

            return ResponseHandler.generateResponse(HttpStatus.OK, "contact", contact);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null, e.getMessage());
        }
    }
}
