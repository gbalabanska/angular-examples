package com.chat.controllers;

import com.chat.dto.response.MessageDTO;
import com.chat.entities.Message;
import com.chat.entities.ChannelUser;
import com.chat.reponse.ApiResponse;
import com.chat.repositories.ChannelUserRepository;
import com.chat.repositories.MessageRepository;
import com.chat.services.ChannelService;
import com.chat.util.CookieExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/chat-channel")
public class ChatChannelController {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ChannelUserRepository channelUserRepository;

    @Autowired
    private CookieExtractor cookieExtractor;

    // Load all messages for a channel
    @Autowired
    private ChannelService channelService;

    @GetMapping("/{channelId}/messages")
    public ResponseEntity<ApiResponse<List<MessageDTO>>> getChannelMessages(@PathVariable int channelId) {
        List<MessageDTO> messages = channelService.getMessagesWithUsername(channelId);

        if (messages.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(null, "No messages found in this channel."));
        }

        return ResponseEntity.ok(new ApiResponse<>(messages, "Messages retrieved successfully."));
    }

    // Send a new message to the channel
    @PostMapping("/{channelId}/messages")
    public ResponseEntity<ApiResponse<Message>> sendChannelMessage(@PathVariable int channelId, @RequestBody Message message, HttpServletRequest request) {
        int userId = cookieExtractor.extractUserId(request); // Get user ID from the request
        Optional<ChannelUser> channelUser = channelUserRepository.findActiveChannelUserByChannelAndUser(channelId, userId);

        // Check if the user is not a member or has been removed from the channel
        if (channelUser.isEmpty() || channelUser.get().isUserRemoved()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(null, "User is not authorized to send messages in this channel."));
        }

        // Populate message fields
        message.setSenderId(userId);
        message.setChannelId(channelId);
        message.setCreatedAt(LocalDateTime.now());

        Message savedMessage = messageRepository.save(message);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(savedMessage, "Message sent successfully."));
    }
}
