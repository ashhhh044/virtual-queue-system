package com.queue.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.queue.config.JwtUtil;
import com.queue.data_transfer_object_dto.LoginRequest;
import com.queue.model.Admin;
import com.queue.model.Staff;
import com.queue.repository.AdminRepository;
import com.queue.repository.StaffRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")

public class AuthController {
    private StaffRepository staffRepository;
    private AdminRepository adminRepository;
    private final JwtUtil jwtUtil;
    
    public AuthController(StaffRepository staffRepository, AdminRepository adminRepository, JwtUtil jwtUtil){
        this.staffRepository = staffRepository;
        this.adminRepository = adminRepository;
        this.jwtUtil = jwtUtil;
    }

    //Login Endpoint - POST /api/auth/login
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest request) {
        try {
            Map<String, Object> response = new HashMap<>();

            Staff staff = staffRepository.findByEmail(request.getEmail()).orElse(null);
            if(staff != null && staff.getPassword().equals(request.getPassword())){
                // Staff authenticated
                String token = jwtUtil.generateToken(staff.getEmail(), "STAFF");
                response.put("success", true);
                response.put("message", "Login Successfull");
                response.put("token", token);
                response.put("role", "STAFF");
                response.put("name", staff.getName());
                response.put("staffId", staff.getId());
                return ResponseEntity.ok(response);
            }

             Admin admin = adminRepository.findByEmail(request.getEmail()).orElse(null);
            
            if (admin != null && admin.getPassword().equals(request.getPassword())) {
                // Admin authenticated
                String token = jwtUtil.generateToken(admin.getEmail(), "ADMIN");
                
                response.put("success", true);
                response.put("message", "Login successful");
                response.put("token", token);
                response.put("role", "ADMIN");
                response.put("name", admin.getName());
                response.put("adminId", admin.getId());
                
                return ResponseEntity.ok(response);
            }

            // No match found

            response.put("success", false);
            response.put("message", "Invalid email or password");
            return ResponseEntity.status(401).body(response);

        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Login failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Validate token - GET /api/auth/validate
    @GetMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateToken(@RequestHeader("Authorization") String authHeader) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                response.put("success", false);
                response.put("message", "Invalid token format");
                return ResponseEntity.status(401).body(response);
            }
            
            String token = authHeader.substring(7);
            
            if (jwtUtil.validateToken(token)) {
                String email = jwtUtil.extractUsername(token);
                String role = jwtUtil.extractRole(token);
                
                response.put("success", true);
                response.put("valid", true);
                response.put("email", email);
                response.put("role", role);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("valid", false);
                response.put("message", "Token expired or invalid");
                return ResponseEntity.status(401).body(response);
            }
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("valid", false);
            response.put("message", "Invalid token");
            return ResponseEntity.status(401).body(response);
        }
    }    
}
