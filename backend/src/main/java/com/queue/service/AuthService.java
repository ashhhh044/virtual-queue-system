package com.queue.service;

import com.queue.model.Admin;
import com.queue.model.Staff;
import com.queue.model.User;
import com.queue.repository.AdminRepository;
import com.queue.repository.StaffRepository;

public class AuthService {
    
    private final StaffRepository staffRepository;
    private final AdminRepository adminRepository;

    public AuthService(StaffRepository staffRepository, AdminRepository adminRepository){
        this.staffRepository = staffRepository;
        this.adminRepository = adminRepository;
    }

    // authenticate admin and staff by email and password
     public Staff authenticateStaff(String email, String password) {
        Staff staff = staffRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Staff not found"));
        
        // Check password
        if (!staff.getPassword().equals(password)) {
            throw new RuntimeException("Invalid password");
        }
        
        return staff;
    }

    public Admin authenticateAdmin(String email, String password) {
        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
        
        // Check password
        if (!admin.getPassword().equals(password)) {
            throw new RuntimeException("Invalid password");
        }
        
        return admin;
    }

    //  Authenticate user (either Staff or Admin) by email and password
    public User authenticateUser(String email, String password) {
        // Try to find as Staff first
        try {
            Staff staff = staffRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            if (staff.getPassword().equals(password)) {
                return staff;
            }
        } catch (Exception e) {
            // Staff not found, try Admin
        }
        
        // Try to find as Admin
        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (!admin.getPassword().equals(password)) {
            throw new RuntimeException("Invalid password");
        }
        
        return admin;
    }

    // Check if email exists
    public boolean emailExists(String email) {
        // Check in both Staff and Admin repositories
        // (since User doesn't have password anymore)
        return staffRepository.findByEmail(email).isPresent() 
                || adminRepository.findByEmail(email).isPresent();
    }
}
