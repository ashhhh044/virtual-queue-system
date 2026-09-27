package com.queue.repository;

import com.queue.model.Services;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ServiceRepository extends JpaRepository<Services, Long> {
    
    // Find service by name (exact match)
    Optional<Services> findByName(String name);
    
    // Find services by name containing (search)
    List<Services> findByNameContainingIgnoreCase(String name);
    
    // Find all active services
    List<Services> findByIsActiveTrue();
    
    // Find all inactive services
    List<Services> findByIsActiveFalse();
}