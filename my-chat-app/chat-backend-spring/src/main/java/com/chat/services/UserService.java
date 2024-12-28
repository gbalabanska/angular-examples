package com.chat.services;

import com.chat.dto.SignUpRequest;
import com.chat.entities.User;
import com.chat.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void signUp(SignUpRequest signUpRequest) {
        // Check if username already exists
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new IllegalArgumentException("Username already taken");
        }

        // Hash the password
        String encodedPassword = passwordEncoder.encode(signUpRequest.getPassword());

        // Create a new user
        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setPassword(encodedPassword);

        // Save the user to the database
        userRepository.save(user);
    }
}
