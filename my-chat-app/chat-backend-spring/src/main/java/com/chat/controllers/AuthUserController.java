package com.chat.controllers;

import com.chat.dto.AddNewUserRequest;
import com.chat.dto.AuthRequest;
import com.chat.entities.User;
import com.chat.reponse.ApiResponse;
import com.chat.services.JwtService;
import com.chat.services.UserInfoService;
import com.chat.util.CookieExtractor;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
public class AuthUserController {

    @Autowired
    private UserInfoService service;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    CookieExtractor cookieExtractor;

    @GetMapping("/currentUser")
    public ResponseEntity<Map<String, Object>> getCurrentUser(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();

        response.put("userId", cookieExtractor.extractUserId(request));
        response.put("username", cookieExtractor.extractUsername(request));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Endpoint for testing purposes only
    @GetMapping("/welcome")
    public ResponseEntity<Map<String, String>> welcome(HttpServletRequest request) {
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

        Date expirationDate = jwtService.extractExpiration(token);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Welcome user " + username + ". Your token will expire at " + expirationDate);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/addNewUser")
    public ResponseEntity<ApiResponse<Void>> addNewUser(@Valid @RequestBody AddNewUserRequest newUserDTO) {
        User newUser = new User();
        newUser.setUsername(newUserDTO.getUsername());
        newUser.setPassword(newUserDTO.getPassword());
        newUser.setRoles("ROLE_USER");

        // Call the service to add user
        String result = service.addUser(newUser);

        // Return a successful response
        ApiResponse<Void> response = new ApiResponse<>(null, result);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/generateToken")
    public ResponseEntity<Map<String, String>> authenticateAndGetToken(@RequestBody AuthRequest authRequest, HttpServletResponse response) {
        // Authenticate the user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );

        if (authentication.isAuthenticated()) {
            // Generate the JWT token
            String token = jwtService.generateToken(authRequest.getUsername());

            // Create the cookie with HttpOnly and Secure flags
            Cookie cookie = new Cookie("token", token);
            cookie.setHttpOnly(true);  // Makes the cookie inaccessible to JavaScript
            cookie.setSecure(true);    // Ensures cookie is only sent over HTTPS
            cookie.setPath("/");       // The cookie is available for all paths on the domain
            cookie.setMaxAge(60 * 60); // Expire in 60min

            // Add the cookie to the response
            response.addCookie(cookie);

            // Return a success message
            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("message", "Authentication successful. Token stored in cookie.");
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } else {
            throw new UsernameNotFoundException("Invalid user request!");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(HttpServletResponse response) {
        // Create a cookie with the same name as the JWT cookie
        Cookie cookie = new Cookie("token", null); // Set value to null to clear it
        cookie.setHttpOnly(true);                  // Ensure HttpOnly is set
        cookie.setSecure(true);                    // Ensure Secure is set
        cookie.setPath("/");                       // Match the original path
        cookie.setMaxAge(0);                       // Set cookie to expire immediately
        response.addCookie(cookie);                // Add the expired cookie to the response

        ApiResponse<String> apiResponse = new ApiResponse<>("You have been logged out successfully.");
        return ResponseEntity.ok(apiResponse);
    }
}