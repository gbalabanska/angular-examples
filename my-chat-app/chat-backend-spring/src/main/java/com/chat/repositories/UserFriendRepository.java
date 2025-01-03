package com.chat.repositories;

import com.chat.entities.UserFriend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserFriendRepository extends JpaRepository<UserFriend, Integer> {

    // Custom query to find if a friendship exists between two users
    boolean existsByUserIdAndFriendId(int userId, int friendId);
}
