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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Entity
@Table(name = "conversations")
@Getter
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; 

    @Column(nullable = true)
    @Size(min = 5, max = 50, message = "Name must be between 5 and 50 characters")
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ConversationType type;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(
        name="users_conversations",
        joinColumns={@JoinColumn(name="CONVERSATION_ID", referencedColumnName="ID")},
        inverseJoinColumns={@JoinColumn(name="USER_ID", referencedColumnName="ID")}
    )
    @JsonIgnoreProperties(value = "conversations")
    private List<User> participants = new ArrayList<User>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)    
    @JoinColumn(name = "conversation_id")
    private List<Message> messages = new ArrayList<Message>();

    public List<Message> messages() {
        return messages;
    }

    public List<User> participants() {
        return participants;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParticipants(List<User> participants) {
        this.participants = participants;
    }

    public void setType(ConversationType type) {
        this.type = type;
    }
}
