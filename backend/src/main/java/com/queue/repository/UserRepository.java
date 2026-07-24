package com.queue.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.queue.model.User;

public interface UserRepository extends JpaRepository<User, Long>{

    // Find user by email -- for login
    Optional<User> findByEmail(String email);

    // Check if email exists
    boolean existsByEmail(String email);

     // Find by role (for filtering)
    Optional<User> findByRole(String role);
}
