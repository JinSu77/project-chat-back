package com.example.api.services.Message;

import java.util.List;
import java.util.Optional;

import com.example.api.models.Message;

public interface IMessagesService {
    List<Message> getAllMessagesFromChannel(Integer channelId);
    List<Message> getAllMessagesFromConversation(Integer conversationId);
    Message getMessageById(Integer id);
    Optional<Message> findMessageById(Integer id);
    void delete(Integer id);
    void save(Message message);
}
