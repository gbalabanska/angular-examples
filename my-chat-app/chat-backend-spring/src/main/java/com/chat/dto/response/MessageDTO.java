package com.chat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO {

    private int id;

    private int senderId; // Reference to the sender user

    private String senderUsername; // Username of the sender

    private int receiverId; // Reference to the receiver user, nullable for channel messages

    private int channelId; // Reference to the channel (nullable for direct messages)

    private String messageText; // The content of the message

    private LocalDateTime createdAt; // The time the message was created
}