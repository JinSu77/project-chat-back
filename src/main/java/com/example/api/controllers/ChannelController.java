package com.example.api.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.api.handlers.ResponseHandler;
import com.example.api.models.Channel;
import com.example.api.services.Channel.ChannelService;

@Controller
@RequestMapping("/channels")
public class ChannelController {
    @Autowired
    private ChannelService channelService;

    @GetMapping
    public ResponseEntity<Object> index() {
        List<Channel> channels = channelService.findAllChannels();
        
        return ResponseHandler.generateResponse(HttpStatus.OK, "channels", channels);
    }
    
    @GetMapping("/{channelId}")
    public ResponseEntity<Object> show(@PathVariable Integer channelId) {
        Optional<Channel> channel = channelService.findChannelById(channelId);

        if (channel.isEmpty()) {
            return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND, null, "Channel not found");
        }

        return ResponseHandler.generateResponse(HttpStatus.OK, "channel", channel.get());
    }
}
