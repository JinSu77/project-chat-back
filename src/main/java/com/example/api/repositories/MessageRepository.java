package com.example.api.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.example.api.models.Message;


public interface MessageRepository extends CrudRepository<Message, Integer> {
    Optional<Message> findByIOptional(String id);
}
