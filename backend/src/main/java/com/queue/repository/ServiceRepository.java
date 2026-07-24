package com.queue.repository;

import com.queue.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ServiceRepository extends JpaRepository<Service, Long> {
    
    // Find service by name (exact match)
    Optional<Service> findByName(String name);
    
    // Find services by name containing (search)
    List<Service> findByNameContainingIgnoreCase(String name);
    
    // Find all active services
    List<Service> findByIsActiveTrue();
    
    // Find all inactive services
    List<Service> findByIsActiveFalse();
}