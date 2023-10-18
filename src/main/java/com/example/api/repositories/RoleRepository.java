package com.example.api.repositories;

import org.springframework.data.repository.CrudRepository;

import com.example.api.models.Role;

public interface RoleRepository extends CrudRepository<Role, Integer> {
    Role findByName(String name);
}