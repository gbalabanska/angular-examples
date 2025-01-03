package com.chat.util;

import com.chat.services.JwtService;
import com.chat.services.UserInfoService;
import com.chat.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CookieExtractor {
    @Autowired
    private UserInfoService service;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserService userService;

    public String extractUsername(HttpServletRequest request) {
        // Retrieve the token from the cookie
        String token = null;
        String username = null;

        // Look for the "token" cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    username = jwtService.extractUsername(token);  // Extract username from the token
                    break;
                }
            }
        }

        if (token == null || !jwtService.validateToken(token, service.loadUserByUsername(username))) {
            throw new IllegalArgumentException("Invalid or missing token");
        }

        return username;
    }

    // Method to find user ID from username
    public int extractUserId(HttpServletRequest request) {
        String username = extractUsername(request);  // Extract the username
        if (username == null) {
            throw new IllegalArgumentException("Username not found in token");
        }

        // Use the UserService to get the user by username
        return userService.getUserIdByUsername(username);  // Assuming getUserIdByUsername is implemented in UserService
    }}
