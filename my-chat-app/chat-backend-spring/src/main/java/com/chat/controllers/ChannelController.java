package com.chat.controllers;

import com.chat.entities.Channel;
import com.chat.services.ChannelService;
import com.chat.util.CookieExtractor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/channels")
public class ChannelController {

    @Autowired
    CookieExtractor cookieExtractor;
    @Autowired
    ChannelService channelService;

    @PostMapping("/create/{channelName}")
    public ResponseEntity<Map<String, String>> createChannel(@PathVariable String channelName, HttpServletRequest request) {
        int userIdRequest = cookieExtractor.extractUserId(request);
        System.out.println("----------------- createChannel from userID: " + userIdRequest);
        Channel newChannel = new Channel();
        newChannel.setName(channelName);
        newChannel.setOwnerId(userIdRequest);

        Map<String, String> response = new HashMap<>();

        try {
            // Try to save the channel and return a response
            channelService.saveChannel(newChannel);
            response.put("message", "Channel added successfully!");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            response.put("message", "Channel already exists!");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
