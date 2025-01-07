package com.chat.errors;

public class ChannelPermissionException extends RuntimeException {
    public ChannelPermissionException() {
        super("User lacks the required permissions to perform this action on the channel.");
    }

    public ChannelPermissionException(String message) {
        super(message);
    }
}
