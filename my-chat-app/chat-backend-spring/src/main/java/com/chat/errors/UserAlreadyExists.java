package com.chat.errors;

public class UserAlreadyExists extends RuntimeException {
    public UserAlreadyExists() {
        super("User Already Exists.");
    }

    // Constructor with a custom message
    public UserAlreadyExists(String message) {
        super(message);
    }
}
