package com.queue.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.queue.model.Customer;
import com.queue.service.QueueService;



@RestController
@RequestMapping("/api/staff")
@CrossOrigin(origins = "*")

public class StaffController {

    private final QueueService queueService;

    public StaffController (QueueService queueService){
        this.queueService = queueService;
    }

    // get current queue - GET /api/staff/queue/{serviceType}
    @GetMapping("/queue/{serviceType}")
    public ResponseEntity<Map<String, Object>> getQueue(@PathVariable String serviceType) {
        try {
            List<Customer> queue = queueService.getCurrentQueue(serviceType);
            Map<String, Object> response = new HashMap<>();

            response.put("success", true);
            response.put("serviceType", serviceType);
            response.put("queueSize", queue.size());
            response.put("customers", queue);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();

            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    

    // call next customer - POST /api/staff/call-next/{serviceType}
    @PostMapping("/call-next/{serviceType}")
    public ResponseEntity<Map<String, Object>> callNext(@PathVariable String serviceType) {
        try {

            Customer next = queueService.getNextCustomer(serviceType);

            if (next == null) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "No customers in queue");
                return ResponseEntity.badRequest().body(response);
            }

            Map<String, Object> response = new HashMap<>();

            response.put("success", true);
            response.put("message", "Next customer called");
            response.put("customer", next);
            response.put("tokenNumber", next.getTokenNumber());
            response.put("name", next.getName());

            return ResponseEntity.ok(response);
        } catch (Exception e) {

            Map<String, Object> response = new HashMap<>();

            response.put("success", false);
            response.put("message", e.getMessage());

            return ResponseEntity.badRequest().body(response);
        }
        
    }    

    // complete service - PUT /api/staff/complete/{customerId}
    @PutMapping("/complete/{customerId}")
    public ResponseEntity<Map<String, Object>> completeService(@PathVariable Long customerId) {
        try {

            Customer customer = queueService.completeService(customerId);

            Map<String, Object> response = new HashMap<>();

            response.put("success", true);
            response.put("message", "Service Compeleted for: " + customer.getName());
            response.put("customer", customer);

            return ResponseEntity.ok(response);
        } catch (Exception e) {

            Map<String, Object> response = new HashMap<>();

            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }       
        
    }
}
