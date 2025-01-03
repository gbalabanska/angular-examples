package com.chat.services;

import com.chat.entities.Channel;
import com.chat.repositories.ChannelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChannelService {

    @Autowired
    ChannelRepository channelRepository;

    public Channel saveChannel(Channel channel) {
        // Perform any business logic or validation here if needed
        // For example, check if a channel with the same name already exists
        if (channelRepository.existsByName(channel.getName())) {
            throw new IllegalArgumentException("Channel with the same name already exists");
        }

        // Save the channel to the database
        return channelRepository.save(channel);
    }

}
