package com.example.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import com.example.api.dtos.Messages.MessageDTO;
import com.example.api.handlers.ResponseHandler;
import com.example.api.models.Message;
import com.example.api.services.Auth.JwtUtil;
import com.example.api.services.Message.MessagesService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/conversations/{conversationId}/messages")
public class ConversationMessageController {
    @Autowired
    private MessagesService messagesService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping
    public ResponseEntity<Object> index(@PathVariable("conversationId") Integer conversationId)
    {
        try {
            List<Message> messages = messagesService.getAllMessagesFromConversation(conversationId);

            return ResponseHandler.generateResponse(HttpStatus.OK, "messages", messages);
        } catch (ResponseStatusException responseStatusException) {
            return ResponseHandler.generateResponse(responseStatusException, null, responseStatusException.getMessage());
        } catch (Exception e) {
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null, e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<Object> store(
        @Valid @RequestBody MessageDTO messageDTO, 
        @RequestHeader("Authorization") String authorization, 
        @PathVariable("conversationId") Integer conversationId
    ) {
        try {
            Integer userId = jwtUtil.getAuthUserId(authorization);

            Message message = messageDTO.toMessage(conversationId, userId, null);

            messagesService.save(message);
        
            return ResponseHandler.generateResponse(HttpStatus.OK, "message", message);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null, e.getMessage());
        }
    }
}
