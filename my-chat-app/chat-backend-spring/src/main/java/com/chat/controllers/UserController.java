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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    public ResponseEntity<Map<String, String>> addFriend(@PathVariable int id, HttpServletRequest request) {
        // Get the username from the token (extracted from cookies)
        int userIdRequest = cookieExtractor.extractUserId(request);
        System.out.println(">>>>>>>>>>>>>>>>>>>>>> addFriend for userId: " + userIdRequest);

        Map<String, String> response = new HashMap<>();

        // Add the friend (add the user with ID `id` to the friend list of the user)
        boolean isFriendAdded = userService.addFriend(userIdRequest, id);

        if (isFriendAdded) {
            response.put("message", "Friend added successfully!");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.put("message", "You are already friends with this user!");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

}
