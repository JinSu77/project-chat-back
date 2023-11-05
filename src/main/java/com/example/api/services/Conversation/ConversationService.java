package com.example.api.services.Conversation;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.api.models.Conversation;
import com.example.api.models.User;
import com.example.api.repositories.ConversationRepository;

@Service
public class ConversationService implements IConversationService {
    @Autowired ConversationRepository conversationRepository;

    public Optional<Conversation> findUserConversationById(User user, Integer conversationId) {
        Optional<Conversation> conversation = user.getConversations()
            .stream()
            .filter((conv) -> conv.getId() == conversationId)
            .findFirst();
        
        return conversation;
    }

    public void save(Conversation conversation) {
        conversationRepository.save(conversation);
    }
}

