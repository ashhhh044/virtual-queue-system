package com.queue.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.queue.data_transfer_object_dto.JoinQueueRequest;
import com.queue.model.Customer;
import com.queue.model.Services;
import com.queue.service.AdminService;
import com.queue.service.QueueService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/customer")
@CrossOrigin(origins = "*")
public class CustomerController {
    
    private final QueueService queueService;
    private final AdminService adminService;

    public CustomerController(QueueService queueService, AdminService adminService){
        this.queueService = queueService;
        this.adminService = adminService;
    }

    //list active services - GET /api/customer/services
    // Public on purpose: customers need to know what services exist before
    // they can join a queue, and shouldn't need an admin login to see that.

    @GetMapping("/services")
    public ResponseEntity<Map<String,Object>> getActiveServices(){
        try{
            List<Services> services = adminService.getActiveServices();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("services", services);

            return ResponseEntity.ok(response);
        } catch(Exception e){
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    
    // join queue - POST /api/customer/join

    @PostMapping("/join")
    public ResponseEntity<Map<String, Object>> joinQueue(@Valid @RequestBody JoinQueueRequest request) {
            
        try {
            
            System.out.println("Join request received: " + request.getName());
            
            Customer customer = queueService.joinQueue(
                request.getName(),
                request.getEmail(),
                request.getPhone(),
                request.getServiceType(),
                request.getPriority() != null ? request.getPriority() : "normal"
            );
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Successfully added to the queue");
            response.put("customer", customer);
            response.put("tokenNumber", customer.getTokenNumber());
            response.put("position", customer.getPosition());
            response.put("eta", customer.getEta());
            response.put("accessKey", customer.getAccessKey());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
        
    }
    
    // get queue status - GET /api/customer/status/{accessKey}

    @GetMapping("/status/{accessKey}")
    public ResponseEntity<Map<String, Object>> getStatus(@PathVariable String accessKey) {
        try {
            Customer customer = queueService.getCustomerStatus(accessKey);

            Map<String, Object> response = new HashMap<>();

            response.put("success", true);
            response.put("customer", customer);
            response.put("tokenNumber", customer.getTokenNumber());
            response.put("position", customer.getPosition());
            response.put("eta", customer.getEta());
            response.put("status", customer.getStatus());
            response.put("joinedAt", customer.getJoinedAt());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Customer not found");

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }    

    // cancel queue spot - DELETE /api/customer/cancel/{accessKey}

    @DeleteMapping("/cancel/{accessKey}")
    public ResponseEntity<Map<String, Object>> cancelQueue(@PathVariable String accessKey){
        try {
            boolean cancelled = queueService.cancelQueueSpot(accessKey);

            Map<String, Object> response = new HashMap<>();

            if(cancelled){
                response.put("success", true);
                response.put("message", "Successfully cancelled spot in queue");
            }
            else{
                response.put("success", false);
                response.put("message", "Operation failed - Spot not found");
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {

            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());

            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/test")
    public ResponseEntity<Map<String, Object>> test() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "✅ Customer API is working!");
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(response);
    }
}
