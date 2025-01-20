package com.chat.controllers;

import com.chat.dto.response.Friend;
import com.chat.reponse.ApiResponse;
import com.chat.services.FriendService;
import com.chat.util.CookieExtractor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/friends")
public class FriendsController {


    @Autowired
    CookieExtractor cookieExtractor;
    @Autowired
    FriendService friendService;

    @GetMapping("")
    public ResponseEntity<ApiResponse<List<Friend>>> getFriendListForUser(HttpServletRequest request) {
        int userId = cookieExtractor.extractUserId(request);
        List<Friend> friends = friendService.getFriendList(userId);
        return ResponseEntity.ok(new ApiResponse<>(friends, "Friend list retrieved successfully."));
    }

}
