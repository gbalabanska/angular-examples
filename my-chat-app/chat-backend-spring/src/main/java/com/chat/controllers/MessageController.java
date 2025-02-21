package com.chat.controllers;

import com.chat.entities.Message;
import com.chat.services.MessageService;
import com.chat.util.CookieExtractor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
/*
INSERT INTO message (sender_id, receiver_id, channel_id, message_text, created_at)
VALUES (35, 36, 0, 'Hey thereqqqqqqqq! How are you?', '2025-01-20 10:00:00');
 */

    @Autowired
    private MessageService messageService;

    @Autowired
    private CookieExtractor cookieExtractor;

    // Get all messages between current user and friend
    @GetMapping
    public ResponseEntity<List<Message>> getMessages(
            @RequestParam int friendId,
            HttpServletRequest request) {

        // Get userId from JWT token using CookieExtractor
        int userId = cookieExtractor.extractUserId(request);

        if (userId == 0) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Fetch messages between current user and the specified friend
        List<Message> messages = messageService.getMessages(userId, friendId);
        return ResponseEntity.ok(messages);
    }

    // Send a new message
    @PostMapping
    public ResponseEntity<Message> sendMessage(@RequestBody Message message, HttpServletRequest request) {
        int userId = cookieExtractor.extractUserId(request);

        if (userId == 0) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        message.setSenderId(userId);

        // Save the message
        Message savedMessage = messageService.sendMessage(message);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMessage);
    }
}
