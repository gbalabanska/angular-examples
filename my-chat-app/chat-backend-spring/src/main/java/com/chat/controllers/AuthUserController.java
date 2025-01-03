package com.chat.controllers;

import com.chat.dto.AddNewUserRequest;
import com.chat.dto.AuthRequest;
import com.chat.entities.User;
import com.chat.services.JwtService;
import com.chat.services.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.*;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
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

        // Get expiration date or other token info
        Date expirationDate = jwtService.extractExpiration(token);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Welcome user " + username + ". Your token will expire at " + expirationDate);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

//TODO: da ne se pozvolqwa potrebitel sus sushiq USERNAME!!!!!
    @PostMapping("/addNewUser")
    public ResponseEntity<Map<String, String>> addNewUser(@Valid @RequestBody AddNewUserRequest newUserDTO) {
        // Form new user from DTO
        User newUser = new User();
        newUser.setUsername(newUserDTO.getUsername());
        newUser.setPassword(newUserDTO.getPassword());
        newUser.setRoles("ROLE_USER");

        String result = service.addUser(newUser);

        // Create a map to return as JSON response
        Map<String, String> response = new HashMap<>();
        response.put("message", result);  // Example: {"message": "User Added"}

        return new ResponseEntity<>(response, HttpStatus.OK);  // Returns JSON response with HTTP status 200 (OK)
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
            cookie.setMaxAge(24 * 60 * 60); // Expire in 24 hours (adjust as needed)

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

}