package com.queue.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.queue.model.Customer;


public interface CustomerRepository extends JpaRepository<Customer, Long>{
    
    // find customer by access-key -- for customer self-service
    Optional<Customer> findByAccessKey(String accessKey);

    // find customers based on status -- waiting, called, serviced, no-show
    List<Customer> findByStatus(String status);

    // find customers based on priority
    List<Customer> findByPriority(String priority);

    // find customers "waiting" in the queue
    List<Customer> findByStatusOrderByJoinedAtAsc(String status);

    // check if access key exists
    boolean existsByAccessKey(String accessKey);
}
