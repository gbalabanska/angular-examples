package com.chat.services;

import com.chat.dto.response.Friend;
import com.chat.entities.User;
import com.chat.entities.UserFriend;
import com.chat.errors.UserNotFoundException;
import com.chat.repositories.UserFriendRepository;
import com.chat.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FriendService {
    @Autowired
    UserFriendRepository userFriendRepository;
    @Autowired
    UserRepository userRepository;

    public List<Friend> getFriendList(int userId) {
        System.out.println("userId=" + userId);

        List<Friend> friendList = new ArrayList<>();
        List<UserFriend> userFriendList = userFriendRepository.findFriendshipsByUserId(userId);

        for (UserFriend uf : userFriendList) {
            // Determine the friend's ID
            int friendId = (uf.getUserId() == userId) ? uf.getFriendId() : uf.getUserId();

            // Fetch the friend's User entity
            Optional<User> user = userRepository.findById(friendId);

            if (user.isEmpty()) {
                throw new UserNotFoundException("User not found with ID: " + friendId);
            }

            // Create a Friend DTO and populate it
            Friend friend = new Friend();
            friend.setFriendId(friendId);
            friend.setFriendUsername(user.get().getUsername());

            friendList.add(friend);
        }

        return friendList;
    }

}
