package com.example.api.repositories;


import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.api.models.User;


public interface UserRepository extends CrudRepository<User, Integer> {
    User findByUsername(String username);
    User findByEmail(String email);
    List<User> findAll();
}