package com.example.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import com.example.api.handlers.ResponseHandler;
import com.example.api.models.Message;
import com.example.api.services.Message.MessagesService;

@Controller
@RequestMapping("/messages")
public class MessageController {
    @Autowired
    private MessagesService messagesService;

    @GetMapping("/{messageId}")
    public ResponseEntity<Object> show(@PathVariable("messageId") int messageId)   
    {  
        try {
            Message message = messagesService.getMessageById(messageId);

            return ResponseHandler.generateResponse(HttpStatus.OK, "message", message);
        } catch (ResponseStatusException responseStatusException) {
            return ResponseHandler.generateResponse(responseStatusException, null, responseStatusException.getMessage());
        } catch (Exception e) {
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null, e.getMessage());
        }
    }  

    @DeleteMapping("/{messageId}")  
    public ResponseEntity<Object> delete(@PathVariable("messageId") int messageId)
    {  
        try {
            messagesService.delete(messageId);  

            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (ResponseStatusException responseStatusException) {
            return ResponseHandler.generateResponse(responseStatusException, null, responseStatusException.getMessage());
        } catch (Exception e) {
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null, e.getMessage());
        }
    }  
}
