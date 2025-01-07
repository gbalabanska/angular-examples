package com.chat.errors;

public class ChannelNotFoundException extends RuntimeException {

    // Default constructor
    public ChannelNotFoundException() {
        super("Channel not found.");
    }

    // Constructor with a custom message
    public ChannelNotFoundException(String message) {
        super(message);
    }
}
