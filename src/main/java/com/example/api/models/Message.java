package com.example.api.models;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "messages")
@Getter
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = true)
    private String content;

    @Column(nullable = true)
    private Integer conversation_id;

    @Column(nullable = false)
    private Integer user_id;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @CreationTimestamp
    private Date created_at;

    @Column(nullable = true)
    private Date received_at;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "channel_id", nullable = true)
    @JsonIgnoreProperties({"messages"})
    private Channel channel = null;

    public void setContent(String content) {
        this.content = content;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public void setConversationId(Integer conversation_id) {
        this.conversation_id = conversation_id;
    }

    public void setUserId(Integer user_id) {
        this.user_id = user_id;
    }

    public void setCreatedAt(Date created_at) {
        this.created_at = created_at;
    }
}
