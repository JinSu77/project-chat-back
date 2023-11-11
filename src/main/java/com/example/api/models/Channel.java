package com.example.api.models;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Entity
@Table(name = "channels")
@Getter
public class Channel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    @Size(min = 2, max = 50)
    private String name;

    @OneToMany(mappedBy = "channel")
    private List<Message> messages;

    public List<Message> messages() {
        return messages;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
