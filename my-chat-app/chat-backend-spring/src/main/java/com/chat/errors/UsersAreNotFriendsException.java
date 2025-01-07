package com.chat.errors;

public class UsersAreNotFriendsException extends RuntimeException {
    // Default constructor
    public UsersAreNotFriendsException() {
        super("You cannot add users to the channel if they are not in your friend list!");
    }

    // Constructor with a custom message
    public UsersAreNotFriendsException(String message) {
        super(message);
    }
}
