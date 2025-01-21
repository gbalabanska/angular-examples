package com.chat.repositories;

import com.chat.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Integer> {
    // Custom query to find messages by channelId
    List<Message> findByChannelId(int channelId);
    List<Message> findByReceiverIdAndSenderIdOrSenderIdAndReceiverId(int receiverId, int senderId, int senderId2, int receiverId2);
}
