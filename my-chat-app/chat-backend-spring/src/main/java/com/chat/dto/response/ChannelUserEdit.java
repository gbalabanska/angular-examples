package com.chat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChannelUserEdit { // represents one member of a channel
    private int userId;
    private String username;
    private String userRole;
}
