package com.practice.kafka.event;

public class MessageEvent {
    private String key;
    private String message;

    public MessageEvent(String key, String message) {
        this.key = key;
        this.message = message;
    }

    public String getKey() {
        return key;
    }

    public String getMessage() {
        return message;
    }
}
