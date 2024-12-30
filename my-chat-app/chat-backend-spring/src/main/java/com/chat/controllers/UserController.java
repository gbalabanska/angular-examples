package com.chat.controllers;

import com.chat.dto.AuthRequest;
import com.chat.entities.User;
import com.chat.services.JwtService;
import com.chat.services.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserInfoService service;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/welcome")
    public String welcome(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");

        // Optionally validate token here
        if (!jwtService.validateToken(token, service.loadUserByUsername(jwtService.extractUsername(token)))) {
            throw new IllegalArgumentException("Invalid token");
        }

        String username = jwtService.extractUsername(token);
        Date expirationDate = jwtService.extractExpiration(token);

        return "Welcome user " + username + ". Your token will expire at " + expirationDate;
    }



    @PostMapping("/addNewUser")
    public ResponseEntity<Map<String, String>> addNewUser(@RequestBody User userInfo) {
        String result = service.addUser(userInfo);  // Assuming addUser returns a success message

        // Create a map to return as JSON response
        Map<String, String> response = new HashMap<>();
        response.put("message", result);  // Example: {"message": "User Added"}

        return new ResponseEntity<>(response, HttpStatus.OK);  // Returns JSON response with HTTP status 200 (OK)
    }

    @GetMapping("/user/userProfile")
    public String userProfile() {
        return "Welcome to User Profile";
    }

    @GetMapping("/admin/adminProfile")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String adminProfile() {
        return "Welcome to Admin Profile";
    }

    @PostMapping("/generateToken")
    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(authRequest.getUsername());
        } else {
            throw new UsernameNotFoundException("Invalid user request!");
        }
    }

}