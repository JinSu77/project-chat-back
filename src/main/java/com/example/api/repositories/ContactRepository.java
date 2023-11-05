package com.example.api.repositories;

import org.springframework.data.repository.CrudRepository;

import com.example.api.models.Contact;

public interface ContactRepository extends CrudRepository<Contact, Integer> {
}
