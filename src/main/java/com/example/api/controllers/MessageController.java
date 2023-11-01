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
import com.example.api.models.Message;
import com.example.api.services.Auth.JwtUtil;
import com.example.api.services.Message.MessagesService;
import com.example.api.validation.Messages.MessageDTO;

import jakarta.validation.Valid;


@Controller
@RequestMapping("/messages")
public class MessageController {
    @Autowired
    MessagesService messagesService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping
    private ResponseEntity<Object> getAllMessages()
    {
        List<Message> messages = messagesService.getAllMessages();

        Map<String, Object> response = Map.of(
            "messages", messages
        );

        return ResponseHandler.generateResponse(HttpStatus.OK, response);
    }

    @GetMapping("/{messageId}")
    private ResponseEntity<Object> getSingleMessage(@PathVariable("messageId") int messageId)   
    {  
        Message message = messagesService.getMessageById(messageId);

        Map<String, Object> response = Map.of(
            "message", message
        );

        return ResponseHandler.generateResponse(HttpStatus.OK, response);  
    }  

    @DeleteMapping("/{messageId}")  
    private ResponseEntity<Object> deleteMessage(@PathVariable("messageId") int messageId)
    {  
        try {
            messagesService.delete(messageId);  

            return ResponseHandler.generateResponse(HttpStatus.NO_CONTENT, null);  
        } catch (Exception e) {
            return ResponseHandler.generateResponse(HttpStatus.NO_CONTENT, null);
        }
      
    }  

    @PostMapping
    private ResponseEntity<Object> createMessage(@Valid @RequestBody MessageDTO messageDTO, @RequestHeader("Authorization") String authorization)
    {
        try {
            Integer user_id = jwtUtil.getAuthUserId(authorization);

            messageDTO.setUser_id(user_id);

            Message savedMessage = messagesService.saveMessage(messageDTO);

            Message message = messagesService.getMessageById(savedMessage.getId());

            Map<String, Object> response = Map.of(
                "message", message
            );
        
            return ResponseHandler.generateResponse(HttpStatus.OK, response);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        }
    }
}
