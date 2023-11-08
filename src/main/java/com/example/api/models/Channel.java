package com.example.api.models;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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

    /* on delete cascade */
    @ManyToMany(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinTable(
        name="channels_messages",
        joinColumns={@JoinColumn(name="CHANNEL_ID", referencedColumnName="ID")},
        inverseJoinColumns={@JoinColumn(name="MESSAGE_ID", referencedColumnName="ID")}
    )
    private List<Message> messages = new ArrayList<Message>();

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
