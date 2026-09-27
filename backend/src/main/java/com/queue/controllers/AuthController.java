package com.queue.controllers;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.queue.config.JwtUtil;
import com.queue.data_transfer_object_dto.LoginRequest;
import com.queue.data_transfer_object_dto.RefreshRequest;
import com.queue.model.Admin;
import com.queue.model.Staff;
import com.queue.model.User;
import com.queue.repository.AdminRepository;
import com.queue.repository.StaffRepository;
import com.queue.repository.UserRepository;

import jakarta.validation.Valid;

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
    private final PasswordEncoder passwordEncoder;
    private static final long REFRESH_TOKEN_VALIDITY_DAYS = 7;
    private UserRepository userRepository;
    
    public AuthController(StaffRepository staffRepository, AdminRepository adminRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder, UserRepository userRepository){
        this.staffRepository = staffRepository;
        this.adminRepository = adminRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    //Login Endpoint - POST /api/auth/login
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginRequest request) {
        try {
            Map<String, Object> response = new HashMap<>();

            Staff staff = staffRepository.findByEmail(request.getEmail()).orElse(null);
            if(staff != null && passwordEncoder.matches(request.getPassword(), staff.getPassword())){
                // Staff authenticated
                String token = jwtUtil.generateToken(staff.getEmail(), "STAFF");
                String refreshToken = java.util.UUID.randomUUID().toString();
                staff.setRefreshToken(refreshToken);
                staff.setRefreshTokenExpiry(LocalDateTime.now().plusDays(REFRESH_TOKEN_VALIDITY_DAYS));
                staffRepository.save(staff);

                response.put("refreshToken", refreshToken);
                response.put("success", true);
                response.put("message", "Login Successfull");
                response.put("token", token);
                response.put("role", "STAFF");
                response.put("name", staff.getName());
                response.put("staffId", staff.getId());
                return ResponseEntity.ok(response);
            }

             Admin admin = adminRepository.findByEmail(request.getEmail()).orElse(null);
            
            if (admin != null && passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
                // Admin authenticated
                String token = jwtUtil.generateToken(admin.getEmail(), "ADMIN");
                String refreshToken = java.util.UUID.randomUUID().toString();
                admin.setRefreshToken(refreshToken);
                admin.setRefreshTokenExpiry(LocalDateTime.now().plusDays(REFRESH_TOKEN_VALIDITY_DAYS));
                adminRepository.save(admin);
                
                response.put("refreshToken", refreshToken);
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
    
    @PostMapping("/refresh")
    public ResponseEntity<Map<String, Object>> refresh(@RequestBody RefreshRequest request) {
        
        Map<String, Object> response = new HashMap<>();
        User user = userRepository.findByRefreshToken(request.getRefreshToken()).orElse(null);

        if(user == null || user.getRefreshTokenExpiry() == null || user.getRefreshTokenExpiry().isBefore(LocalDateTime.now())){
            response.put("success", false);
            response.put("message", "Invalid or expired refresh token - please log in again");
            return ResponseEntity.status(401).body(response);
        }

        String newAccessToken = jwtUtil.generateToken(user.getEmail(), user.getRole());
        response.put("success", true);
        response.put("token", newAccessToken);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(@RequestBody RefreshRequest request) {

        userRepository.findByRefreshToken(request.getRefreshToken()).ifPresent(user ->{
            user.setRefreshToken(null);
            user.setRefreshTokenExpiry(null);
            userRepository.save(user);
        });

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        
        return ResponseEntity.ok(response);
    }   
}
