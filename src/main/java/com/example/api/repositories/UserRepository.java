package com.example.api.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.example.api.models.User;


public interface UserRepository extends CrudRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsernameOrEmail(String username, String email);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}