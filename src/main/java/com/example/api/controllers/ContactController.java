package com.example.api.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.api.handlers.ResponseHandler;
import com.example.api.models.Contact;
import com.example.api.services.Contact.ContactService;
import com.example.api.validation.Contacts.ContactDTO;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/contacts")
public class ContactController {
    @Autowired
    ContactService contactService;

    @GetMapping
    private ResponseEntity<Object> index()
    {
        List<Contact> contacts = contactService.getAllContacts();

        Map<String, Object> response = Map.of(
            "contacts", contacts
        );

        return ResponseHandler.generateResponse(HttpStatus.OK, response);
    }

    @GetMapping("/{contactId}")
    private ResponseEntity<Object> show(@PathVariable("contactId") int contactId)   
    {  
        Contact contact = contactService.getContactById(contactId);

        Map<String, Object> response = Map.of(
            "contact", contact
        );

        return ResponseHandler.generateResponse(HttpStatus.OK, response);  
    }

    @DeleteMapping("/{contactId}")
    private ResponseEntity<Object> delete(@PathVariable("contactId") int contactId)   
    {  
        try {
            contactService.delete(contactId);

            return ResponseHandler.generateResponse(HttpStatus.NO_CONTENT, null);  
        } catch (Exception e) {
            return ResponseHandler.generateResponse(HttpStatus.NO_CONTENT, null);  
        }
    }

    @PostMapping
    private ResponseEntity<Object> store(@Valid @RequestBody ContactDTO contactDTO, @RequestHeader("Authorization") String authorization)
    {
        try {
            Contact contactCreated = contactService.saveContact(contactDTO);

            Contact contact = contactService.getContactById(contactCreated.getId());

            Map<String, Object> response = Map.of(
                "contact", contact
            );

            return ResponseHandler.generateResponse(HttpStatus.CREATED, response);

        } catch (Exception e) {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST, null);
        }
    }

}
