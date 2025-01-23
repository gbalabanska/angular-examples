package com.chat.controllers;

import com.chat.dto.friends.AddFriendToChannel;
import com.chat.dto.response.AvailableChannel;
import com.chat.dto.response.ChannelUserEdit;
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
    public ResponseEntity<ApiResponse<List<AvailableChannel>>> getChannelsForUser(HttpServletRequest request) {
        int userId = cookieExtractor.extractUserId(request);

        // Fetch channels associated with the user using the service method
        List<AvailableChannel> availableChannels = channelService.findChannelsForUser(userId);

        ApiResponse<List<AvailableChannel>> response = new ApiResponse<>(
                availableChannels,
                "Channels fetched successfully"
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    // Update Channel Name
    @PostMapping("/update-name/{channelId}/{newChannelName}")
    public ResponseEntity<ApiResponse<Void>> updateChannelName(
            @PathVariable int channelId,
            @PathVariable String newChannelName,
            HttpServletRequest request) {

        int userIdRequest = cookieExtractor.extractUserId(request);

        channelService.updateChannelName(channelId, userIdRequest, newChannelName);

        return ResponseEntity.ok(new ApiResponse<>("Channel name updated successfully!"));
    }


    // Delete Channel
    @DeleteMapping("/delete/{channelId}")
    public ResponseEntity<ApiResponse<Void>> deleteChannel(
            @PathVariable int channelId,
            HttpServletRequest request) {

        int userIdRequest = cookieExtractor.extractUserId(request);

        // Check if the user is the owner of the channel
        Channel channel = channelService.getChannelById(channelId);
        if (channel != null && channel.getOwnerId() == userIdRequest) {
            if (channelService.deleteChannel(channelId)) {
                return ResponseEntity.ok(new ApiResponse<>("Channel deleted successfully!"));
            } else {
                return ResponseEntity.badRequest().body(new ApiResponse<>("Channel was already deleted!"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>("You are not authorized to delete this channel."));
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
            return new ResponseEntity<>(new ApiResponse("Friend was successfully added to the channel!"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ApiResponse("User was not added to the channel!"), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/users/{channelId}")
    public ResponseEntity<ApiResponse<List<ChannelUserEdit>>> getActiveChannelUsers(@PathVariable int channelId) {
        List<ChannelUserEdit> channelUsers = channelService.getChannelUsers(channelId);
        ApiResponse<List<ChannelUserEdit>> response = new ApiResponse<>(channelUsers, "Channel users were successfully fetched");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("name/{channelId}")
    public ResponseEntity<ApiResponse<String>> getChannelName(@PathVariable int channelId) {
        String channelName = channelService.getChannelNameById(channelId);
        ApiResponse<String> response = new ApiResponse<>(channelName, "Channel name.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //promote channel user to an admin
    @PostMapping("/promote")
    public ResponseEntity<ApiResponse<Void>> promoteUserToAdmin(
            @RequestParam int channelId,
            @RequestParam int userIdToPromote,
            HttpServletRequest request) {

        int loggedInUserId = cookieExtractor.extractUserId(request);

        channelService.promoteUserToAdmin(channelId, loggedInUserId, userIdToPromote);

        return ResponseEntity.ok(new ApiResponse<>("User promoted to admin successfully!"));
    }

    @DeleteMapping("/remove-user/{channelId}/{userIdToRemove}")
    public ResponseEntity<ApiResponse<Void>> removeUserFromChannel(
            @PathVariable int channelId,
            @PathVariable int userIdToRemove,
            HttpServletRequest request) {
        int loggedInUserId = cookieExtractor.extractUserId(request);
        channelService.removeUserFromChannel(channelId, userIdToRemove, loggedInUserId);
        return ResponseEntity.ok(new ApiResponse<>(null, "User removed from the channel successfully."));
    }


}
