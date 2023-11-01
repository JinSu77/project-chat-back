package com.example.api.repositories;

import org.springframework.data.repository.CrudRepository;

import com.example.api.models.Conversation;

public interface ConversationRepository extends CrudRepository<Conversation, Integer> {
}
