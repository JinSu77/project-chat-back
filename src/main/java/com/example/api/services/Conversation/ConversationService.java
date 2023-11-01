package com.example.api.services.Conversation;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.api.models.Conversation;
import com.example.api.models.User;

@Service
public class ConversationService implements IConversationService {
    public Optional<Conversation> findUserConversationById(User user, Integer conversationId) {
        Optional<Conversation> conversation = user.getConversations()
            .stream()
            .filter((conv) -> conv.getId() == conversationId)
            .findFirst();
        
        return conversation;
    }
}

