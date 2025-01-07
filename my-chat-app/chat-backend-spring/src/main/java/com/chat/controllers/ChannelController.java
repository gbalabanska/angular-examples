package com.chat.controllers;

import com.chat.dto.UserChannelDTO;
import com.chat.dto.friends.AddFriendToChannel;
import com.chat.entities.Channel;
import com.chat.reponse.ApiResponse;
import com.chat.services.ChannelService;
import com.chat.util.CookieExtractor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
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

    @GetMapping("/user/channels")
    public ResponseEntity<Map<String, Object>> getChannelsForUser(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();

        int userId = cookieExtractor.extractUserId(request);

        // Fetch the channels associated with the user using the service method
        List<UserChannelDTO> userChannels = channelService.findChannelsForUser(userId);

        // Add the result to the response map
        response.put("channels", userChannels);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    // Update Channel Name
    @PostMapping("/update/{channelId}")
    public ResponseEntity<Map<String, String>> updateChannelName(
            @PathVariable int channelId,
            @RequestBody Map<String, String> requestBody,
            HttpServletRequest request) {

        int userIdRequest = cookieExtractor.extractUserId(request);
        String newChannelName = requestBody.get("newChannelName");
        Map<String, String> response = new HashMap<>();

        // Check if the user is the owner of the channel
        Channel channel = channelService.getChannelById(channelId);
        if (channel != null && channel.getOwnerId() == userIdRequest) {
            boolean channelIsUpdated = channelService.updateChannelName(channelId, newChannelName);
            if (channelIsUpdated) {
                response.put("message", "Channel name updated successfully!");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("message", "Could not update this channel. The name may be already taken or the channel does not exist.");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        } else {
            response.put("message", "You are not authorized to update this channel.");
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }

    }

    // Delete Channel
    @DeleteMapping("/delete/{channelId}")
    public ResponseEntity<Map<String, String>> deleteChannel(
            @PathVariable int channelId,
            HttpServletRequest request) {

        int userIdRequest = cookieExtractor.extractUserId(request);
        Map<String, String> response = new HashMap<>();

        // Check if the user is the owner of the channel
        Channel channel = channelService.getChannelById(channelId);
        if (channel != null && channel.getOwnerId() == userIdRequest) {
            if (channelService.deleteChannel(channelId)) {
                response.put("message", "Channel deleted successfully!");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("message", "Channel was already deleted!");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        } else {
            response.put("message", "You are not authorized to delete this channel.");
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/addFriends/{channelId}")
    public ResponseEntity<ApiResponse> addFriendsToChannel(
            @PathVariable int channelId,
            @RequestBody AddFriendToChannel friendId,
            HttpServletRequest request) {

        int userIdRequest = cookieExtractor.extractUserId(request);

        boolean friendIsAddedToChannel = channelService.addFriendToChannel(friendId.getFriendId(), channelId, userIdRequest);
        if (friendIsAddedToChannel) {
            return new ResponseEntity<>(new ApiResponse("Friend was successfully added to the channel!", true), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ApiResponse("User was not added to the channel!", false), HttpStatus.BAD_REQUEST);

        }
    }


}
