package com.example.api.models;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

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

    @Column(nullable = false)
    private Integer user_id;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date created_at;

    @Column(nullable = true)
    private Date received_at;

    public void setContent(String content) {
        this.content = content;
    }

    public void setUserId(Integer user_id) {
        this.user_id = user_id;
    }

    public void setCreatedAt(Date created_at) {
        this.created_at = created_at;
    }
}
