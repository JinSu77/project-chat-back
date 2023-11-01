package com.example.api.services.Message;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.api.models.Message;
import com.example.api.repositories.MessageRepository;
import com.example.api.validation.Messages.MessageDTO;

@Service
public class MessagesService implements IMessagesService {
    @Autowired
    MessageRepository messageRepository;

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

    public Message saveMessage(MessageDTO messageDTO) {
        Message message = messageDTO.toMessage();

        return messageRepository.save(message);
    }
}