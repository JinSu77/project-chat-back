package com.example.api.controllers;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.api.models.Message;
import com.example.api.repositories.MessageRepository;


@Controller
@RequestMapping("/messages")
public class MessageController {
    @Autowired

    private MessageRepository messageRepository;

    @GetMapping()
    public @ResponseBody ResponseEntity<Object> getAllMessages() {
        Iterable<Message> messages = messageRepository.findAll();

        HashMap<String, Object> response = new HashMap<String, Object>();

        for (Message message : messages) {
            response.put(message.getId(), message.toArray());
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/{id}")
    public @ResponseBody ResponseEntity<Object> getOneMessage(@PathVariable("id") String id) {
        Message msg = messageRepository.findById(Integer.parseInt(id)).get();
        return ResponseEntity.ok(msg.toArray());
    }
}
