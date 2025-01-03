package com.chat.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChannelUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int channelId; // Reference to Channel ID

    private int userId; // Reference to User ID

    private String role; // e.g., OWNER, ADMIN, MEMBER, GUEST

    private boolean isDeleted; // Flag to check if the user is removed from the channel
}
