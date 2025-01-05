package com.chat.controllers;

import com.chat.dto.AddFriendsRequest;
import com.chat.dto.UserChannelDTO;
import com.chat.entities.Channel;
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

        try {
            // Check if the user is the owner of the channel
            Channel channel = channelService.getChannelById(channelId);
            if (channel != null && channel.getOwnerId() == userIdRequest) {
                channel.setName(newChannelName);
                channelService.saveChannel(channel);
                response.put("message", "Channel name updated successfully!");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("message", "You are not authorized to update this channel.");
                return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
            }
        } catch (Exception ex) {
            response.put("message", "Error updating channel name.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
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
    public ResponseEntity<Map<String, String>> addFriendsToChannel(
            @PathVariable int channelId,
            @RequestBody AddFriendsRequest friendsIdToAdd,
            HttpServletRequest request) {

        int userIdRequest = cookieExtractor.extractUserId(request);
        Map<String, String> response = new HashMap<>();
        if (channelService.isUserAdminOfChannel(userIdRequest, channelId) ||
                channelService.isUserOwnerOfChannel(userIdRequest, channelId)) {
            System.out.println("==================== User is ADMIN or OWNER ====================");

            channelService.addFriendsToChannel(friendsIdToAdd.getFriendsIdtoAddList(), channelId, userIdRequest);

            response.put("message", "Friends were successfully added to the channel!");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            // User is neither ADMIN nor OWNER
            response.put("message", "You are not authorized to add members to this channel.");
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }

    }

}
