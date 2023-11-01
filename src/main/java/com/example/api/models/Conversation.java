package com.example.api.models;

import com.example.api.enums.ConversationType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "conversations")
@Getter
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; 

    @Column(nullable = true)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ConversationType type;

    public void setName(String name) {
        this.name = name;
    }

    public void setType(ConversationType type) {
        this.type = type;
    }
}
