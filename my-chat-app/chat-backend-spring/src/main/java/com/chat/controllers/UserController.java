package com.chat.controllers;

import com.chat.entities.User;
import com.chat.services.JwtService;
import com.chat.services.UserInfoService;
import com.chat.services.UserService;
import com.chat.util.CookieExtractor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserInfoService service;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserService userService;
    @Autowired
    CookieExtractor cookieExtractor;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username, HttpServletRequest request) {
        Optional<User> user = userService.getUserByUsername(username);

        // Check if the user is found or not
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

   @PostMapping("/addFriend/{id}")
   public ResponseEntity<String> addFriend(@PathVariable int id, HttpServletRequest request) {
        // Get the username from the token (extracted from cookies)
        String username = cookieExtractor.extractUsername(request);
        System.out.println("----------------- addFriend for user: "+username);
        // Get the user who is sending the friend request (the user initiating the action)
        Optional<User> requestingUserOptional = userService.getUserByUsername(username);

        if (!requestingUserOptional.isPresent()) {
            return ResponseEntity.status(404).body("User not found");
        }

        User requestingUser = requestingUserOptional.get();
        int requestingUserId = requestingUser.getId();

        // Add the friend (add the user with ID `id` to the friend list of the user)
        boolean isFriendAdded = userService.addFriend(requestingUserId, id);

        if (isFriendAdded) {
            return ResponseEntity.ok("Friend added successfully");
        } else {
            return ResponseEntity.status(400).body("You are already friends with this user");
        }
    }

}
