package com.example.api.services.Message;

import java.util.List;

import com.example.api.models.Message;

public interface IMessagesService {
    List<Message> getAllMessages();
    void delete(Integer id);
    void save(Message message);
}
