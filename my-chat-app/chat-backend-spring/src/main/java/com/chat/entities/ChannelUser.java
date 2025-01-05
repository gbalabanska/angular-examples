package com.chat.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "channel_user")
public class ChannelUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int channelId; // Reference to Channel ID

    private int userId; // Reference to User ID

    private String role; // e.g., OWNER, ADMIN, MEMBER, GUEST

    private boolean isChannelDeleted = false; // Flag to check if the user is removed from the channel
    private boolean isUserRemoved = false;
}
