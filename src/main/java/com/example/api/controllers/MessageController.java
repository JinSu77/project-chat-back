package com.example.api.controllers;

import java.util.List;
import java.util.Optional;

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

        return ResponseHandler.generateResponse(HttpStatus.OK, "messages", messages);
    }

    @GetMapping("/{messageId}")
    public ResponseEntity<Object> show(@PathVariable("messageId") int messageId)   
    {  
        Message message = messagesService.getMessageById(messageId);

        return ResponseHandler.generateResponse(HttpStatus.OK, "message", message);
    }  

    @DeleteMapping("/{messageId}")  
    public ResponseEntity<Object> delete(@PathVariable("messageId") int messageId)
    {  
        try {
            Optional<Message> message = messagesService.findMessageById(messageId);

            if (message.isEmpty()) {
                return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND, null, "Message not found");
            }

            messagesService.delete(messageId);  

            return ResponseHandler.generateResponse(HttpStatus.NO_CONTENT, null, null);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null, e.getMessage());
        }
      
    }  
}
