package com.example.api.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
@RequestMapping("/conversations/{conversationId}/messages")
public class ConversationMessageController {
    @Autowired
    private MessagesService messagesService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<Object> store(
        @Valid @RequestBody MessageDTO messageDTO, 
        @RequestHeader("Authorization") String authorization, 
        @PathVariable("conversationId") Integer conversationId
    ) {
        try {
            Integer userId = jwtUtil.getAuthUserId(authorization);

            Message message = messageDTO.toMessage(conversationId, userId);

            messagesService.save(message);

            Map<String, Object> response = Map.of(
                "message", message
            );
        
            return ResponseHandler.generateResponse(HttpStatus.OK, response);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        }
    }
}
