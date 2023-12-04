package com.example.api.controllers;

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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import com.example.api.dtos.Messages.MessageDTO;
import com.example.api.handlers.ResponseHandler;
import com.example.api.models.Channel;
import com.example.api.models.Message;
import com.example.api.models.User;
import com.example.api.services.Auth.JwtUtil;
import com.example.api.services.Channel.ChannelService;
import com.example.api.services.Message.MessagesService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/channels/{channelId}/messages")
public class ChannelMessageController {
    @Autowired
    private ChannelService channelService;

    @Autowired
    private MessagesService messagesService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping
    public ResponseEntity<Object> index(@PathVariable("channelId") Integer channelId)
    {
        try {
            List<Message> messages = messagesService.getAllMessagesFromChannel(channelId);

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
        @PathVariable("channelId") Integer channelId
    ) {
        try {
            Optional<Channel> channel = channelService.findChannelById(channelId);

            if (channel.isEmpty()) {
                return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND, null, "Channel not found");
            }

            String token = authorization.replace("Bearer ", "");

            User user = jwtUtil.getAuthUser(token);

            Message message = messageDTO.toMessage(null, user.getId(), user.getUsername(), channel.get());

            messagesService.save(message);

            channelService.updateMessageRelationship(channel.get(), message);

            return ResponseHandler.generateResponse(HttpStatus.CREATED, "message", message);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null, e.getMessage());
        }
    }
}
