package com.chat.repositories;

import com.chat.entities.ChannelUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChannelUserRepository extends JpaRepository<ChannelUser, Integer> {

    List<ChannelUser> findByChannelId(int channelId);
    List<ChannelUser> findByUserId(int userId);

    // find channels of a user where user is not removed and channel is not deleted
    List<ChannelUser> findByUserIdAndIsChannelDeletedFalseAndIsUserRemovedFalse(int userId);

    Optional<ChannelUser> findByChannelIdAndUserIdAndIsChannelDeletedFalseAndIsUserRemovedFalse(int channelId, int userId);
}
