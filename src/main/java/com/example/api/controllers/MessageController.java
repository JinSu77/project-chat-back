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
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.api.handlers.ResponseHandler;
import com.example.api.models.Message;
import com.example.api.services.Message.MessagesService;

@Controller
@RequestMapping("/messages")
public class MessageController {
    @Autowired
    private MessagesService messagesService;

    @GetMapping
    public ResponseEntity<Object> index()
    {
        List<Message> messages = messagesService.getAllMessages();

        Map<String, Object> response = Map.of(
            "messages", messages
        );

        return ResponseHandler.generateResponse(HttpStatus.OK, response);
    }

    @GetMapping("/{messageId}")
    public ResponseEntity<Object> show(@PathVariable("messageId") int messageId)   
    {  
        Message message = messagesService.getMessageById(messageId);

        Map<String, Object> response = Map.of(
            "message", message
        );

        return ResponseHandler.generateResponse(HttpStatus.OK, response);  
    }  

    @DeleteMapping("/{messageId}")  
    public ResponseEntity<Object> delete(@PathVariable("messageId") int messageId)
    {  
        try {
            messagesService.delete(messageId);  

            return ResponseHandler.generateResponse(HttpStatus.NO_CONTENT, null);  
        } catch (Exception e) {
            return ResponseHandler.generateResponse(HttpStatus.NO_CONTENT, null);
        }
      
    }  
}
