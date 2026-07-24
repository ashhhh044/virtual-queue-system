package com.queue.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.queue.data_transfer_object_dto.AnalyticsResponse;
import com.queue.data_transfer_object_dto.ServiceRequest;
import com.queue.data_transfer_object_dto.StaffRequest;
import com.queue.model.Service;
import com.queue.model.Staff;
import com.queue.service.AdminService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins="*")
public class AdminController {
    
    private final AdminService adminService;

    public AdminController(AdminService adminService){
        this.adminService = adminService;
    }

    // Service Endpoints

    // Create a new service - POST /api/admin/services
    @PostMapping("/services")
    public ResponseEntity<Map<String, Object>> createService(@RequestBody ServiceRequest request) {
        
        try {

            Service service = adminService.createService(
                request.getName(), request.getDescription(), request.getEstimatedDuration());
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Service created successfully");
            response.put("service", service);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Get all services - GET /api/admin/services
    @GetMapping("/services")
    public ResponseEntity<Map<String, Object>> getAllServices() {
        try {
            List<Service> services = adminService.getAllServices();
            Map<String, Object> response = new HashMap<>();

            response.put("success", true);
            response.put("count", services.size());
            response.put("services", services);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();

            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    // Get Active services - GET /api/admin/services/active
    @GetMapping("/services/active")
    public ResponseEntity<Map<String, Object>> getActiveServices() {
        try {
            List<Service> services = adminService.getActiveServices();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("count", services.size());
            response.put("services", services);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Get services by id - GET /api/admin/services/{id}
    @GetMapping("/services/{id}")
    public ResponseEntity<Map<String, Object>> getServiceById(@PathVariable Long id) {
        try {
            Service service = adminService.getServiceById(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("service", service);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(404).body(response);
        }
    }

    // Update services - PUT /api/admin/services/{id}
    @PutMapping("/services/{id}")
    public ResponseEntity<Map<String, Object>> updateService(@PathVariable("id") Long id, @RequestBody ServiceRequest request) {
        try {

            Service service = adminService.updateService(
                id,
                request.getName(), 
                request.getDescription(), 
                request.getEstimatedDuration(), 
                request.getIsActive()
            );

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Service Updated Successfully");
            response.put("services", service);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // delete service - DELETE /api/admin/services/{id}
    @DeleteMapping("/services/{id}")
    public ResponseEntity<Map<String, Object>> deleteService(@PathVariable Long Id) {
        try {
            adminService.deleteService(Id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Service deleted successfully");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Staff endpoints
    // Create staff - POST /api/admin/staff
    @PostMapping("/staff")
    public ResponseEntity<Map<String, Object>> createStaff(@RequestBody StaffRequest request) {
        try {
            Staff staff = adminService.createStaff(
                request.getName(),
                request.getEmail(),
                request.getPhone(),
                request.getPassword(),
                request.getEmployeeId(),
                request.getDepartment(),
                request.getCounterNumber()
            );
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Staff created successfully");
            response.put("staff", staff);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Get all staff - GET /api/admin/staff
    @GetMapping("/staff")
    public ResponseEntity<Map<String, Object>> getAllStaff() {
        try {
            List<Staff> staff = adminService.getAllStaff();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("count", staff.size());
            response.put("staff", staff);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    // Get staff by id - GET /api/admin/staff/{id}
     @GetMapping("/staff/{id}")
    public ResponseEntity<Map<String, Object>> getStaffById(@PathVariable Long id) {
        try {
            Staff staff = adminService.getStaffById(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("staff", staff);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(404).body(response);
        }
    }
    
    // Update Staff - PUT /api/admin/staff/{id}
    @PutMapping("/staff/{id}")
    public ResponseEntity<Map<String, Object>> updateStaff(
            @PathVariable Long Id,
            @RequestBody StaffRequest request) {
        try {
            Staff staff = adminService.updateStaff(
                Id,
                request.getName(),
                request.getEmail(),
                request.getPhone(),
                request.getPassword(),
                request.getEmployeeId(),
                request.getDepartment(),
                request.getCounterNumber()
            );
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Staff updated successfully");
            response.put("staff", staff);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    //Analytics Report
    //Get analytics - GET /api/admin/stats

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getAnalytics() {
        try {
            AnalyticsResponse analytics = adminService.getAnalytics();

            System.out.println("Analytics response: " + analytics);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("analytics", analytics);
            response.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    
}
