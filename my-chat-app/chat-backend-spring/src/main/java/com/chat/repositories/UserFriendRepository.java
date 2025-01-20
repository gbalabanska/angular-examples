package com.chat.repositories;

import com.chat.entities.UserFriend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserFriendRepository extends JpaRepository<UserFriend, Integer> {

    // Custom query to find if a friendship exists between two users
    boolean existsByUserIdAndFriendId(int userId, int friendId);

    @Query("SELECT uf FROM UserFriend uf WHERE uf.userId = :userId OR uf.friendId = :userId")
    List<UserFriend> findFriendshipsByUserId(@Param("userId") int userId);
}
