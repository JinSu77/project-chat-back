package com.example.api.repositories;

import org.springframework.data.repository.CrudRepository;

import com.example.api.models.User;


public interface UserRepository extends CrudRepository<User, Integer> {
}