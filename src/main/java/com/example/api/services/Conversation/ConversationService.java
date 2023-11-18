package com.example.api.services.Conversation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.api.models.Conversation;
import com.example.api.repositories.ConversationRepository;

@Service
public class ConversationService implements IConversationService {
    @Autowired ConversationRepository conversationRepository;

    public void save(Conversation conversation) {
        conversationRepository.save(conversation);
    }
}

