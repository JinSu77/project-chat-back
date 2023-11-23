package com.example.api.controllers;

import java.util.List;

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
import org.springframework.web.server.ResponseStatusException;

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

        return ResponseHandler.generateResponse(HttpStatus.OK, "contacts", contacts);
    }

    @GetMapping("/{contactId}")
    private ResponseEntity<Object> show(@PathVariable("contactId") int contactId)   
    {  
        try {
            Contact contact = contactService.getContactById(contactId);

            return ResponseHandler.generateResponse(HttpStatus.OK, "contact", contact);
        } catch (ResponseStatusException responseStatusException) {
            return ResponseHandler.generateResponse(responseStatusException, null, responseStatusException.getMessage());
        } catch (Exception e) {
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null, "Contact not found");
        }
    }

    @GetMapping("/random")
    private ResponseEntity<Object> random(@RequestHeader("Authorization") String authorization)
    {
        String token = authorization.replace("Bearer ", "");

        List<Contact> contacts = contactService.getRandomContacts(token);

        return ResponseHandler.generateResponse(HttpStatus.OK, "contacts", contacts);
    }

    @DeleteMapping("/{contactId}")
    private ResponseEntity<Object> delete(@PathVariable("contactId") int contactId)   
    {  
        try {
            contactService.delete(contactId);

            return ResponseEntity.noContent().build();
        } catch (ResponseStatusException responseStatusException) {
            return ResponseHandler.generateResponse(responseStatusException, null, responseStatusException.getMessage());
        } catch (Exception e) {
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null, "Contact not found");
        }
    }

    @PostMapping
    private ResponseEntity<Object> store(@Valid @RequestBody ContactDTO contactDTO, @RequestHeader("Authorization") String authorization)
    {
        try {
            Contact contactCreated = contactService.saveContact(contactDTO);

            Contact contact = contactService.getContactById(contactCreated.getId());

            return ResponseHandler.generateResponse(HttpStatus.CREATED, "contact", contact);
        } catch (ResponseStatusException responseStatusException) {
            return ResponseHandler.generateResponse(responseStatusException, null, responseStatusException.getMessage());
        } catch (Exception e) {
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null, e.getMessage());
        }
    }

}
