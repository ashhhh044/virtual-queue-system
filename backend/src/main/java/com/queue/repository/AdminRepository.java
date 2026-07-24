package com.queue.repository;

import com.queue.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    
    // Find admin by email
    Optional<Admin> findByEmail(String email);
    
}