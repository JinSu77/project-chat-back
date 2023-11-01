package com.example.api.services.Message;

import java.util.List;

import com.example.api.models.Message;
import com.example.api.validation.Messages.MessageDTO;

public interface IMessagesService {
    List<Message> getAllMessages();
    void delete(Integer id);
    Message saveMessage(MessageDTO messageDTO);
}
