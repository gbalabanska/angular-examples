package com.chat.controllers;

import com.chat.dto.SignUpRequest;
import com.chat.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody SignUpRequest signUpRequest) {
        // Sign up the user
        userService.signUp(signUpRequest);
        return ResponseEntity.ok("User registered successfully!");
    }

    @GetMapping("/some-protected-endpoint")
    public String someProtectedEndpoint(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId"); // Access the user ID

        // Now you can use the userId in your business logic
        return "User ID: " + userId;
    }
}
