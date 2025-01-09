package com.chat.repositories;

import com.chat.entities.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChannelRepository extends JpaRepository<Channel, Integer> {
    boolean existsByName(String name);
    Optional<Channel> findByIdAndIsDeletedFalse(int id);



}
