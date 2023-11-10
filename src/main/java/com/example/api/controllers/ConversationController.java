package com.example.api.controllers;

import java.util.ArrayList;
import java.util.List;
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
import com.example.api.validation.Conversations.ConversationDTO;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


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
    public ResponseEntity<Object> index(@RequestHeader("Authorization") String token) {
        Integer userId = jwtUtil.getAuthUserId(token);

        List<Conversation> conversations = userService.findAllConversationsFromUser(userId);

        return ResponseHandler.generateResponse(HttpStatus.OK, "conversations", conversations);
    }

    @GetMapping("/{conversationId}")
    public ResponseEntity<Object> show(@RequestHeader("Authorization") String authorization, @PathVariable("conversationId") Integer conversationId) {

        String token = authorization.replace("Bearer ", "");

        try {
            User user = jwtUtil.getAuthUser(token);

            Optional<Conversation> conversation = conversationService.findUserConversationById(user, conversationId);

            if (conversation.isEmpty()) {
                return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND, null, "Conversation not found");
            }

            return ResponseHandler.generateResponse(HttpStatus.OK, "conversation", conversation);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(HttpStatus.UNPROCESSABLE_ENTITY, null, e.getMessage());
        }
    }

    @PostMapping()
    public ResponseEntity<Object> store(@Valid @RequestBody ConversationDTO conversationDTO, @RequestHeader("Authorization") String authorization) {
        String token = authorization.replace("Bearer ", "");

        List<User> participants = new ArrayList<User>();

        try {
            User authUser = jwtUtil.getAuthUser(token);

            List<User> users = userService.findAllById(conversationDTO.getParticipantIds());

            users.remove(authUser);

            if (users.isEmpty()) {
                return ResponseHandler.generateResponse(HttpStatus.UNPROCESSABLE_ENTITY, null, "You need at least 1 participant");
            }

            participants.add(authUser);
            participants.addAll(users);

            if (participants.size() <= 1) {
                return ResponseHandler.generateResponse(HttpStatus.UNPROCESSABLE_ENTITY, null, "You need at least 2 participants");
            }

            Conversation conversation = conversationDTO.toConversationWithoutParticipants();

            conversation.setParticipants(participants);

            conversationService.save(conversation);

            return ResponseHandler.generateResponse(HttpStatus.OK, "conversation", conversation);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(HttpStatus.UNPROCESSABLE_ENTITY, null, e.getMessage());
        }
    }
}
