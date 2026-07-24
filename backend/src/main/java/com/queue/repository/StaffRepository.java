package com.queue.repository;

import com.queue.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface StaffRepository extends JpaRepository<Staff, Long> {

    // Find staff by email (for login)
    Optional<Staff> findByEmail(String email);
    
    // Find staff by employee ID
    Optional<Staff> findByEmployeeId(String employeeId);
    
    // Find staff by department
    List<Staff> findByDepartment(String department);
    
    // Find staff by counter number (who's working at a specific counter)
    List<Staff> findByCounterNumber(Integer counterNumber);
}