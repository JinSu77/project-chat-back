package com.example.api.controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.api.handlers.ResponseHandler;
import com.example.api.models.Conversation;
import com.example.api.models.User;
import com.example.api.services.Auth.JwtUtil;
import com.example.api.services.Conversation.ConversationService;
import com.example.api.services.User.UserService;

@Controller
@RequestMapping("/conversations")
public class ConversationController {
    @Autowired
    private ConversationService conversationService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping()
    private ResponseEntity<Object> getConversations(@RequestHeader("Authorization") String token) {
        Integer userId = jwtUtil.getAuthUserId(token);

        List<Conversation> conversations = userService.findAllConversationsFromUser(userId);

        Map<String, Object> response = Map.of(
            "conversations", conversations
        );

        return ResponseHandler.generateResponse(HttpStatus.OK, response);
    }

    @GetMapping("/{conversationId}")
    private ResponseEntity<Object> getConversation(@RequestHeader("Authorization") String authorization, @PathVariable("conversationId") Integer conversationId) {

        String token = authorization.replace("Bearer ", "");

        try {
            User user = jwtUtil.getAuthUser(token);

            Optional<Conversation> conversation = conversationService.findUserConversationById(user, conversationId);

            if (conversation.isEmpty()) {
                return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND, null);
            }

            Map<String, Object> response = Map.of(
                "conversation", conversation
            );

            return ResponseHandler.generateResponse(HttpStatus.OK, response);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        }
    }
    
}