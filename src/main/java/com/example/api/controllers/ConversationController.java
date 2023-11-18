package com.example.api.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.api.handlers.ResponseHandler;
import com.example.api.models.Conversation;
import com.example.api.repositories.ConversationRepository;

@Controller
@RequestMapping("/conversations")
public class ConversationController {
    @Autowired
    private ConversationRepository conversationRepository;

    @GetMapping("/{conversationId}")
    public ResponseEntity<Object> show(@PathVariable("conversationId") Integer conversationId) {
        try {
            Optional<Conversation> conversation = conversationRepository.findById(conversationId);

            if (conversation.isEmpty()) {
                return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND, null, "Conversation not found");
            }

            return ResponseHandler.generateResponse(HttpStatus.OK, "conversation", conversation);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null, e.getMessage());
        }
    }
}
