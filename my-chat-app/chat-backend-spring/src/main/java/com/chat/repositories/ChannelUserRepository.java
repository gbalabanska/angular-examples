package com.chat.repositories;

import com.chat.entities.ChannelUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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


    // get info where the user with userId is active member of active channels
    @Query(value = "SELECT * FROM channel_user cu WHERE cu.user_id = :userId AND cu.is_channel_deleted = false AND cu.is_user_removed = false", nativeQuery = true)
    List<ChannelUser> findUserActiveInChannels(@Param("userId") int userId);

    // get active users of channel
    @Query(value = "SELECT * FROM channel_user cu WHERE cu.channel_id = :channelId AND cu.is_user_removed = false", nativeQuery = true)
    List<ChannelUser> getActiveUsersOfChannel(@Param("channelId") int channelId);
}
