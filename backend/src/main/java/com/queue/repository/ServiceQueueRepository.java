package com.queue.repository;

import com.queue.model.ServiceQueue;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ServiceQueueRepository extends JpaRepository<ServiceQueue, Long> {
    
    // Find queue by service type
    Optional<ServiceQueue> findByServiceType(String serviceType);
    
    // Find active queues
    Optional<ServiceQueue> findByStatus(String status);
}