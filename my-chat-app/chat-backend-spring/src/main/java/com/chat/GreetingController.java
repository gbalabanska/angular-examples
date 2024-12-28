package com.chat;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/greetings")
public class GreetingController {

    @GetMapping
    public String getGreeting() {
        return "Hello, World!";
    }

    @PostMapping("/signup1")
    public String getGreeting1() {
        return "Hello, World!";
    }
}