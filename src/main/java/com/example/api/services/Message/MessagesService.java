package com.example.api.services.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.api.models.Message;
import com.example.api.repositories.MessageRepository;

@Service
public class MessagesService implements IMessagesService {
    @Autowired
    MessageRepository messageRepository;

    public Optional<Message> findMessageById(Integer id) {
        return messageRepository.findById(id);
    }
    
    public List<Message> getAllMessages() {
        List<Message> messages = new ArrayList<Message>();
        
        messageRepository.findAll().forEach(messages::add);

        return messages;
    }

    public Message getMessageById(Integer id)   
    {
        Optional<Message> message = messageRepository.findById(id);

        if (message.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Message not found");    
        }

        return message.get();
    }

    public void delete(Integer id)   
    {  
        Optional<Message> message = messageRepository.findById(id);

        if (message.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Message not found");    
        }

        messageRepository.delete(message.get());
    }  

    public void save(Message message) {
        messageRepository.save(message);
    }
}