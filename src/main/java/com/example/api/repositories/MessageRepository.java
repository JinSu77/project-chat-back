package com.example.api.repositories;

import org.springframework.data.repository.CrudRepository;

import com.example.api.models.Message;


public interface MessageRepository extends CrudRepository<Message, Integer>
{
}
