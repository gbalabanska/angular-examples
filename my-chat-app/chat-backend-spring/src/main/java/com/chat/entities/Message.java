package com.chat.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int senderId; // Reference to the sender user

    private int receiverId; // Reference to the receiver user, nullable for channel messages

    private int channelId; // Reference to the channel (nullable for direct messages)

    private String messageText; // The content of the message

    private LocalDateTime createdAt; // Change this from String to LocalDateTime
}
