package com.example.api.controllers;

import java.util.Optional;

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
import com.example.api.models.Channel;
import com.example.api.models.Message;
import com.example.api.services.Auth.JwtUtil;
import com.example.api.services.Channel.ChannelService;
import com.example.api.services.Message.MessagesService;
import com.example.api.validation.Messages.MessageDTO;

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

            Integer userId = jwtUtil.getAuthUserId(authorization);

            Message message = messageDTO.toMessage(null, userId, channelId);

            messagesService.save(message);

            channelService.updateMessageRelationship(channel.get(), message);

            return ResponseHandler.generateResponse(HttpStatus.CREATED, "message", message);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(HttpStatus.UNPROCESSABLE_ENTITY, null, e.getMessage());
        }
    }
}
