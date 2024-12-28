package com.chat.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import com.chat.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);
}