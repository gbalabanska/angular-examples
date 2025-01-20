package com.chat.services;

import com.chat.entities.Message;
import com.chat.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    // Fetch all messages between the current user and a friend
    public List<Message> getMessages(int userId, int friendId) {
        return messageRepository.findByReceiverIdAndSenderIdOrSenderIdAndReceiverId(userId, friendId, userId, friendId);
    }

    // Send a new message
    public Message sendMessage(Message message) {
        message.setCreatedAt(LocalDateTime.now()); // Set current timestamp when sending a message
        return messageRepository.save(message);    // Save the message to the database
    }
}
