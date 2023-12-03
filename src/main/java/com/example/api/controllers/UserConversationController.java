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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import com.example.api.dtos.Conversations.ConversationDTO;
import com.example.api.handlers.ResponseHandler;
import com.example.api.models.Conversation;
import com.example.api.models.User;
import com.example.api.repositories.UserRepository;
import com.example.api.services.Conversation.ConversationService;
import com.example.api.services.User.UserService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/users/{userId}/conversations")
public class UserConversationController {
    @Autowired
    private ConversationService conversationService;

    @Autowired
    private UserService userService;
    
    @Autowired
    private UserRepository userRepository;

    @GetMapping()
    public ResponseEntity<Object> index(@PathVariable("userId") Integer userId) {
        try {
            List<Conversation> conversations = userService.findAllConversationsFromUser(userId);

            return ResponseHandler.generateResponse(HttpStatus.OK, "conversations", conversations);
        } catch (ResponseStatusException responseStatusException) {
            return ResponseHandler.generateResponse(responseStatusException, null, responseStatusException.getMessage());
        } catch (Exception e) {
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null, e.getMessage());
        }
    }

        @PostMapping()
    public ResponseEntity<Object> store(@Valid @RequestBody ConversationDTO conversationDTO, @PathVariable("userId") Integer userId) {
        List<User> participants = new ArrayList<User>();

        try {
            Optional<User> optionalAuthUser = userRepository.findById(userId);

            if (optionalAuthUser.isEmpty()) {
                return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND, null, "User not found");
            }

            User authUser = optionalAuthUser.get();

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
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null, e.getMessage());
        }
    }
}
