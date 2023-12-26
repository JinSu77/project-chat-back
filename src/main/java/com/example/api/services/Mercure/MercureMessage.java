package com.example.api.services.Mercure;

import java.util.UUID;

public class MercureMessage {

    private UUID id;
    private String data;
    private String topic;
    private Boolean isPrivate = false;
    private String type;

    public MercureMessage(String data, String topic) {
        this.data = data;
        this.topic = topic;
        this.id = UUID.randomUUID();
    }

    public MercureMessage(UUID id, String data, String topic, Boolean isPrivate, String type) {
        this.id = id;
        this.data = data;
        this.topic = topic;
        this.isPrivate = isPrivate;
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Boolean getPrivate() {
        return isPrivate;
    }

    public void setPrivate(Boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
