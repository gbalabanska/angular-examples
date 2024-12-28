//package com.chat.controllers;
//
//import com.chat.dto.SignInRequest;
//import com.chat.security.JwtTokenUtil;
//import com.chat.services.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/auth")
//public class AuthenticationController {
//
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    @Autowired
//    private JwtTokenUtil jwtTokenUtil;
//
//    @Autowired
//    private UserService userService;
//
//    @PostMapping("/login")
//    public String login(@RequestBody SignInRequest signInRequest) {
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword())
//        );
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        return jwtTokenUtil.generateToken(authentication.getName());
//    }
//}
