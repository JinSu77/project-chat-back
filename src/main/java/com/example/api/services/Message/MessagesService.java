package com.example.api.services.Message;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.api.models.Channel;
import com.example.api.models.Conversation;
import com.example.api.models.Message;
import com.example.api.repositories.ChannelRepository;
import com.example.api.repositories.ConversationRepository;
import com.example.api.repositories.MessageRepository;

@Service
public class MessagesService implements IMessagesService {
    @Autowired
    ChannelRepository channelRepository;

    @Autowired
    ConversationRepository conversationRepository;
    
    @Autowired
    MessageRepository messageRepository;

    public Optional<Message> findMessageById(Integer id) {
        return messageRepository.findById(id);
    }

    public List<Message> getAllMessagesFromChannel(Integer channelId) {
        Channel channel = channelRepository.findById(channelId)
            .orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Channel not found")
            );

        return channel.getMessages();
    }

    public List<Message> getAllMessagesFromConversation(Integer conversationId) {
        Conversation conversation = conversationRepository.findById(conversationId)
            .orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conversation not found")
            );

        return conversation.getMessages();
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