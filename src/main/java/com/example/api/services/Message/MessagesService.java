package com.example.api.services.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return messageRepository.findById(id).get();
    }

    public void delete(Integer id)   
    {  
        Message message = messageRepository.findById(id).get();

        messageRepository.delete(message);
    }  

    public void save(Message message) {
        messageRepository.save(message);
    }
}