package com.example.api.models;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import java.lang.String;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name="users_roles",
        joinColumns={@JoinColumn(name="USER_ID", referencedColumnName="ID")},
        inverseJoinColumns={@JoinColumn(name="ROLE_ID", referencedColumnName="ID")}
    )
    private List<Role> roles = new ArrayList<Role>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name="users_conversations",
        joinColumns={@JoinColumn(name="USER_ID", referencedColumnName="ID")},
        inverseJoinColumns={@JoinColumn(name="CONVERSATION_ID", referencedColumnName="ID")}
    )
    @JsonIgnoreProperties(value = {"participants", "messages"})
    private List<Conversation> conversations = new ArrayList<Conversation>();

    public List<Conversation> conversations() { 
        return conversations; 
    }

    public void setConversations(List<Conversation> conversations) { 
        this.conversations = conversations; 
    }
}
