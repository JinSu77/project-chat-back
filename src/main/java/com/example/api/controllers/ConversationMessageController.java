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
import com.example.api.exceptions.HubNotFoundException;
import com.example.api.exceptions.PublishRejectedException;
import com.example.api.exceptions.UnauthorizedPublisherException;
import com.example.api.handlers.ResponseHandler;
import com.example.api.models.Message;
import com.example.api.models.User;
import com.example.api.services.Auth.JwtUtil;
import com.example.api.services.Mercure.MercurePublisher;
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
        @RequestHeader("MercureAuthorization") String mercureAuthorization,
        @PathVariable("conversationId") Integer conversationId
    ) {
        try {
            String mercureToken = mercureAuthorization.replace("Bearer ", "");

            String token = authorization.replace("Bearer ", "");

            User user = jwtUtil.getAuthUser(token);

            Message message = messageDTO.toMessage(conversationId, user.getId(), user.getUsername(), null);

            messagesService.save(message);

            var mercurePublisher = new MercurePublisher("http://mercure/.well-known/mercure", mercureToken);

            var mercureMessage = mercurePublisher.create(
                "conversation.message.created", 
                message.toJson(),
                "/conversations/" + conversationId,
                "conversations"
            );

            mercurePublisher.publish(mercureMessage);
        
            return ResponseHandler.generateResponse(HttpStatus.OK, "message", message);
        } catch (UnauthorizedPublisherException|PublishRejectedException|HubNotFoundException e) {
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null, e.getMessage());
        } catch (Exception e) {
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null, e.getMessage());
        }
    }
}
