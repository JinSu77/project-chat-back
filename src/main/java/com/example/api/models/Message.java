package com.example.api.models;

import java.util.HashMap;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = true)
    private String content;

    private Integer user_id;

    public HashMap<String, Object> toArray() {
        HashMap<String, Object> message = new HashMap<String, Object>();

        message.put("id", id);
        message.put("content", content);
        message.put("user_id", user_id);

        return message;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUserId(Integer user_id) {
        this.user_id = user_id;
    }
}
