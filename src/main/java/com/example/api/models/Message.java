package com.example.api.models;

import java.util.HashMap;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = true)
    private String content;

    public HashMap<String, Object> toArray() {
        HashMap<String, Object> message = new HashMap<String, Object>();

        message.put("content", content);

        return message;
    }

    public String getId() {
        return id.toString();
    }
}
