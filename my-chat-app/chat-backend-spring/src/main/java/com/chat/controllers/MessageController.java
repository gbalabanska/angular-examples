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

    @Autowired
    private MessageService messageService;

    @Autowired
    private CookieExtractor cookieExtractor;  // Inject CookieExtractor

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

        // Get userId from JWT token using CookieExtractor
        int userId = cookieExtractor.extractUserId(request);

        if (userId == 0) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Set senderId from the JWT token
        message.setSenderId(userId);

        // Save the message
        Message savedMessage = messageService.sendMessage(message);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMessage);
    }
}
