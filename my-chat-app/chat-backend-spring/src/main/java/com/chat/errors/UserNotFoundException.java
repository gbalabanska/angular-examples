package com.chat.errors;

public class UserNotFoundException extends RuntimeException {

    // Default constructor
    public UserNotFoundException() {
        super("User not found.");
    }

    // Constructor with a custom message
    public UserNotFoundException(String message) {
        super(message);
    }
}
