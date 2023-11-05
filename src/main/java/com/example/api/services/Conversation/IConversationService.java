package com.example.api.services.Conversation;

import java.util.Optional;

import com.example.api.models.Conversation;
import com.example.api.models.User;

public interface IConversationService {
    Optional<Conversation> findUserConversationById(User user, Integer conversationId);
    void save(Conversation conversation);
}
