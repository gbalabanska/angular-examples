package com.chat.services;

import com.chat.dto.UserChannelDTO;
import com.chat.entities.Channel;
import com.chat.entities.ChannelUser;
import com.chat.repositories.ChannelRepository;
import com.chat.repositories.ChannelUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ChannelService {

    @Autowired
    ChannelRepository channelRepository;
    @Autowired
    ChannelUserRepository channelUserRepository;

    public Channel saveChannel(Channel channel) {
        if (channelRepository.existsByName(channel.getName())) {
            throw new IllegalArgumentException("Channel with the same name already exists");
        }
        // Save the channel to the database
        Channel savedChannel = channelRepository.save(channel);         // savedChannel has ID now
        ChannelUser channelUser = new ChannelUser();
        channelUser.setChannelId(channel.getId());
        channelUser.setUserId(channel.getOwnerId());
        channelUser.setRole("OWNER");
        channelUser.setUserRemoved(false);
        channelUser.setChannelDeleted(false);
        channelUserRepository.save(channelUser);
        return savedChannel;
    }

    public Channel getChannelById(int id) {
        Optional<Channel> channel = channelRepository.findById(id);
        return channel.orElse(null);
    }


    public boolean deleteChannel(int channelId) {
        Channel channel = channelRepository.findById(channelId).orElse(null);

        if (channel != null && !channel.isDeleted()) {
            channel.setDeleted(true);
            channelRepository.save(channel);
            // Soft delete the related channel_users
            List<ChannelUser> channelUsers = channelUserRepository.findByChannelId(channelId);
            for (ChannelUser channelUser : channelUsers) {
                channelUser.setChannelDeleted(true);  // Set the flag to true to indicate deletion
                channelUserRepository.save(channelUser);  // Save each updated channel_user
            }
            return true;
        }
        return false;
    }

    public List<UserChannelDTO> findChannelsForUser(int id) {
        List<ChannelUser> userChannels = channelUserRepository.findByUserIdAndIsChannelDeletedFalseAndIsUserRemovedFalse(id);
        if (userChannels.isEmpty()) {
            return null;
        }
        List<UserChannelDTO> userChannelInfoList = new ArrayList<>();
        for (ChannelUser channelUser : userChannels) {
            UserChannelDTO userChannelDTO = new UserChannelDTO();
            userChannelDTO.setRole(channelUser.getRole());
            userChannelDTO.setChannelId(channelUser.getChannelId());
            Channel channel = channelRepository.findById(channelUser.getChannelId()).orElse(null);
            String channelName = channel == null ? "" : channel.getName();
            userChannelDTO.setChannelName(channelName);
            userChannelInfoList.add(userChannelDTO);
        }
        return userChannelInfoList;
    }

    public void addFriendsToChannel(List<Integer> friendIdsList, int channelId, int userId) {
        for (int id : friendIdsList) {
            //todo: dont add friends if users are not existing
            ChannelUser channelUser = new ChannelUser();
            channelUser.setUserId(id);
            channelUser.setChannelId(channelId);
            channelUser.setRole("MEMBER");
            channelUserRepository.save(channelUser);
        }
    }

    //todo: da moje da se iztriqt hora ot daden chanel
//    public boolean removeUserFromChannel(List<Integer> friendIdsList, int channelId, int userId){
//        for(int id: friendIdsList){
//            ChannelUser channelUser = channelUserRepository.findByUserId(id);
//            ChannelUser channelUser = new ChannelUser();
//            channelUser.setUserId(userId);
//            channelUser.setChannelId(channelId);
//            channelUser.setRole("MEMBER");
//            channelUserRepository.save(channelUser);
//        }
//
//        return true;
//    }


    public boolean isUserOwnerOfChannel(int userId, int channelId) {
        System.out.println("-------------------------------------------------------channel id="+channelId + " userid="+userId);
        Optional<Channel> channel = channelRepository.findByIdAndIsDeletedFalse(channelId);
        System.out.println("channel.toString()="+channel.toString());
        if (channel.isPresent()) {
            return channel.get().getOwnerId() == userId;
        }
        return false;
    }

    public boolean isUserAdminOfChannel(int userId, int channelId) {
        Optional<ChannelUser> channelUser = channelUserRepository
                .findByChannelIdAndUserIdAndIsChannelDeletedFalseAndIsUserRemovedFalse(channelId, userId);
        if (channelUser.isPresent()) {
            return channelUser.get().getRole() == "ADMIN";
        }
        return false;
    }
}
