package com.example.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.api.handlers.ResponseHandler;
import com.example.api.models.Message;
import com.example.api.services.Messages.MessagesService;
import com.example.api.validation.Messages.MessageDTO;

import jakarta.validation.Valid;


@RestController
public class MessageController {
    @Autowired
    MessagesService messagesService;

    @GetMapping("/messages")
    private ResponseEntity<Object> getAllMessages()
    {
        List<Message> result = messagesService.getAllMessages();
        return ResponseHandler.generateResponse(HttpStatus.OK, result);
    }

    @GetMapping("/messages/{messageId}")  
    private ResponseEntity<Object> getSingleMessage(@PathVariable("messageId") int messageId)   
    {  
        Message result = messagesService.getMessageById(messageId);
        return ResponseHandler.generateResponse(HttpStatus.OK, result);  
    }  

    @DeleteMapping("/messages/{messageId}")  
    private void deleteMessage(@PathVariable("messageId") int messageId)
    {  
        messagesService.delete(messageId);  
    }  

    @PostMapping("/messages")
    private ResponseEntity<Object> createMessage(@Valid @RequestBody MessageDTO messageDTO)
    {
        try {
            Message savedMessage = messagesService.saveMessage(messageDTO);
            Integer messageId = savedMessage.getId();
            Message message = messagesService.getMessageById(messageId);
        
            return ResponseHandler.generateResponse(HttpStatus.OK, message.toArray());
        } catch (Exception e) {
            return ResponseHandler.generateResponse(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        }
    }
}
