package com.chat.errors;

public class UserAlreadyInChannelException extends RuntimeException {
    public UserAlreadyInChannelException() {
        super("User is already a member of the channel.");
    }

    public UserAlreadyInChannelException(String message) {
        super(message);
    }
}
