package com.chat.services;

import com.chat.dto.response.AvailableChannel;
import com.chat.dto.response.ChannelUserEdit;
import com.chat.entities.Channel;
import com.chat.entities.ChannelUser;
import com.chat.entities.User;
import com.chat.errors.*;
import com.chat.repositories.ChannelRepository;
import com.chat.repositories.ChannelUserRepository;
import com.chat.repositories.UserFriendRepository;
import com.chat.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ChannelService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ChannelRepository channelRepository;
    @Autowired
    ChannelUserRepository channelUserRepository;
    @Autowired
    UserFriendRepository userFriendRepository;

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

    public void updateChannelName(int channelId, int userIdRequest, String newChannelName) {
        Optional<Channel> channelOpt = channelRepository.findByIdAndIsDeletedFalse(channelId);
        if (!channelOpt.isPresent()) {
            throw new ChannelNotFoundException("Channel not found with id: " + channelId);
        }
        Channel channel = channelOpt.get();

        // Check if the user has the necessary role (OWNER or ADMIN) in the channel
        Optional<ChannelUser> channelUserOpt = channelUserRepository.findActiveChannelUserByChannelAndUser(channelId, userIdRequest);
        if (!channelUserOpt.isPresent()) {
            throw new ChannelPermissionException("You are not part of this channel.");
        }
        ChannelUser channelUser = channelUserOpt.get();
        if (!(channelUser.getRole().equals("OWNER") || channelUser.getRole().equals("ADMIN"))) {
            throw new ChannelPermissionException("You don't have permission to update the channel name.");
        }

        if (channelRepository.existsByName(newChannelName)) {
            throw new ChannelPermissionException("The channel name is already taken.");
        }

        channel.setName(newChannelName);
        channelRepository.save(channel);
    }


    public Channel getChannelById(int channelId) {
        Optional<Channel> channel = channelRepository.findById(channelId);
        if (channel.isEmpty()) {
            throw new ChannelNotFoundException();
        }
        return channel.get();
    }

    public String getChannelNameById(int channelId) {
        return getChannelById(channelId).getName();
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

    public List<AvailableChannel> findChannelsForUser(int id) {
        List<ChannelUser> userChannels = channelUserRepository.findByUserIdAndIsChannelDeletedFalseAndIsUserRemovedFalse(id);
        if (userChannels.isEmpty()) {
            return null;
        }
        List<AvailableChannel> availableChannels = new ArrayList<>();
        for (ChannelUser channelUser : userChannels) {
            AvailableChannel availableChannel = new AvailableChannel();
            availableChannel.setChannelId(channelUser.getChannelId());
            Channel channel = channelRepository.findById(channelUser.getChannelId()).orElse(null);
            String channelName = channel == null ? "" : channel.getName();
            if (channelName == null || channelName.equals("")) {
                throw new ChannelNotFoundException();
            }
            availableChannel.setChannelName(channelName);
            availableChannels.add(availableChannel);
        }
        return availableChannels;
    }

    public boolean addFriendToChannel(int friendId, int channelId, int userId) {
        // Check if the channel exists
        if (channelRepository.findById(channelId).isEmpty()) {
            throw new ChannelNotFoundException();
        }

        // Check if the user exists
        if (userRepository.findById(friendId).isEmpty()) {
            throw new UserNotFoundException();
        }

        // Check if the user has permission (owner or admin)
        if (!(isUserOwnerOfChannel(userId, channelId) || isUserAdminOfChannel(userId, channelId))) {
            throw new ChannelPermissionException();
        }

        if (!(userFriendRepository.existsByUserIdAndFriendId(friendId, userId)
                || userFriendRepository.existsByUserIdAndFriendId(userId, friendId))) {
            throw new UsersAreNotFriendsException();
        }

        // Check if the friend is already a member of the channel
        Optional<ChannelUser> existingChannelUser = channelUserRepository.findByChannelIdAndUserIdAndIsChannelDeletedFalseAndIsUserRemovedFalse(channelId, friendId);
        if (existingChannelUser.isPresent()) {
            throw new UserAlreadyInChannelException();
        }

        // Add the friend to the channel as a member
        ChannelUser channelUser = new ChannelUser();
        channelUser.setUserId(friendId);
        channelUser.setChannelId(channelId);
        channelUser.setRole("MEMBER");
        channelUserRepository.save(channelUser);

        return true;
    }

    public boolean isUserOwnerOfChannel(int userId, int channelId) {
        Optional<Channel> channel = channelRepository.findByIdAndIsDeletedFalse(channelId);
        System.out.println("channel.toString()=" + channel.toString());
        if (channel.isPresent()) {
            return channel.get().getOwnerId() == userId;
        }
        return false;
    }

    public boolean isUserAdminOfChannel(int userId, int channelId) {
        Optional<ChannelUser> channelUser = channelUserRepository
                .findByChannelIdAndUserIdAndIsChannelDeletedFalseAndIsUserRemovedFalse(channelId, userId);
        if (channelUser.isPresent()) {
            return channelUser.get().getRole().equals("ADMIN");
        }
        return false;
    }

    public List<ChannelUserEdit> getChannelUsers(int channelId) {

        channelRepository.findByIdAndIsDeletedFalse(channelId)
                .orElseThrow(ChannelNotFoundException::new);

        List<ChannelUserEdit> activeChannelUsersForUpdate = new ArrayList<>();

        for (ChannelUser activeChannelUser : channelUserRepository.getActiveUsersOfChannel(channelId)) {
            ChannelUserEdit channelUserEdit = new ChannelUserEdit();
            channelUserEdit.setUserId(activeChannelUser.getUserId());
            channelUserEdit.setUserRole(activeChannelUser.getRole());
            //find username
            Optional<User> user = userRepository.findById(activeChannelUser.getUserId());
            if (user.isPresent()) {
                channelUserEdit.setUsername(user.get().getUsername());
            }
            activeChannelUsersForUpdate.add(channelUserEdit);
        }

        return activeChannelUsersForUpdate;
    }


    public void promoteUserToAdmin(int channelId, int loggedInUserId, int userIdToPromote) {

        // Check if the logged-in user is the owner of the channel
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new ChannelNotFoundException("Channel with ID " + channelId + " not found."));

        if (channel.getOwnerId() != loggedInUserId) {
            throw new ChannelPermissionException("Only the owner of the channel can promote members to admin.");
        }

        // Find the channel user to promote
        ChannelUser userToPromote = channelUserRepository.findActiveChannelUserByChannelAndUser(channelId, userIdToPromote)
                .orElseThrow(() -> new UserNotFoundException("The user to promote was not found in the channel."));

        // Promote the user
        userToPromote.setRole("ADMIN");
        channelUserRepository.save(userToPromote);
    }

    public void removeUserFromChannel(int channelId, int userId, int loggedInUserId) {
        // Fetch the channel from the repository or throw an exception if not found
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new ChannelNotFoundException());

        // Verify if the logged-in user is the channel owner
        if (channel.getOwnerId() != loggedInUserId) {
            throw new ChannelPermissionException("Only the channel owner can remove users.");
        }

        // Check if the user is a member of the channel or throw an exception
        ChannelUser userToRemove = channelUserRepository.findActiveChannelUserByChannelAndUser(channelId, userId)
                .orElseThrow(() -> new UserNotFoundException("User is not a member of this channel."));

        // Prevent the removal of the channel owner
        if (channel.getOwnerId() == userId) {
            throw new ChannelPermissionException("The channel owner cannot be removed.");
        }

        // FOR SOFT DELETE
        userToRemove.setUserRemoved(true);
        channelUserRepository.save(userToRemove);

    }


}
