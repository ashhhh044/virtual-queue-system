package com.queue.repository;

import com.queue.model.ServiceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface ServiceHistoryRepository extends JpaRepository<ServiceHistory, Long> {
    
    // Find history by customer
    List<ServiceHistory> findByCustomerId(Long customerId);
    
    // Find history by staff
    List<ServiceHistory> findByStaffId(Long staffId);
    
    // Find history by service
    List<ServiceHistory> findByServiceId(Long serviceId);
    
    // Find history by status
    List<ServiceHistory> findByStatus(String status);
    
    // Find history between dates
    List<ServiceHistory> findByServedAtBetween(LocalDateTime start, LocalDateTime end);
    
    // Get average service time for a service
    @Query("SELECT AVG(sh.serviceTime) FROM ServiceHistory sh WHERE sh.service.id = :serviceId")
    Double getAverageServiceTime(@Param("serviceId") Long serviceId);
    
    // Get total customers served today
    @Query("SELECT COUNT(sh) FROM ServiceHistory sh WHERE sh.servedAt >= :startDate AND sh.status = 'completed'")
    Long countServedToday(@Param("startDate") LocalDateTime startDate);
}