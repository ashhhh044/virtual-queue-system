package com.queue.controllers;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/hello")
    public Map<String, String> hello() {
        Map<String, String> response = new HashMap<>();

        response.put("message", "Virtual Queue System is Running!");
        response.put("status", "Success!");

        return response;
    }

    @GetMapping("/ping")
    public Map<String, Object> ping() {
        Map<String, Object> response = new HashMap<>();

        response.put("success", true);
        response.put("timestamp", System.currentTimeMillis());
        response.put("message", "pong");
        
        return response;
    }
    
    
}
