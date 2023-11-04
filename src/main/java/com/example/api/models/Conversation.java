package com.example.api.models;

import java.util.ArrayList;
import java.util.List;

import com.example.api.enums.ConversationType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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

    @JsonIgnoreProperties(value = "conversations")
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(
        name="users_conversations",
        joinColumns={@JoinColumn(name="CONVERSATION_ID", referencedColumnName="ID")},
        inverseJoinColumns={@JoinColumn(name="USER_ID", referencedColumnName="ID")}
    )
    private List<User> users = new ArrayList<User>();

    public List<User> participants() {
        return users;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParticipants(List<User> users) {
        this.users = users;
    }

    public void setType(ConversationType type) {
        this.type = type;
    }
}
