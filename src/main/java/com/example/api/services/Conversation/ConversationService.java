package com.example.api.services.Conversation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.api.enums.ConversationType;
import com.example.api.models.Conversation;
import com.example.api.models.User;
import com.example.api.repositories.ConversationRepository;

@Service
public class ConversationService implements IConversationService {
    @Autowired ConversationRepository conversationRepository;

    public void save(Conversation conversation) {
        conversationRepository.save(conversation);
    }


    public void createConversationBetweenUserAndContact(User userId, User contactId) {
        List<User> participants = new ArrayList<User>();

        participants.add(userId);
        participants.add(contactId);

        Conversation conversation = new Conversation();

        conversation.setType(ConversationType.PRIVATE);

        conversation.setParticipants(participants);

        conversationRepository.save(conversation);
    }
}

