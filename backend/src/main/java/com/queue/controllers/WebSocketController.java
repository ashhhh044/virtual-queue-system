package com.queue.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import com.queue.model.Customer;
import com.queue.model.ServiceQueue;
import com.queue.repository.CustomerRepository;
import com.queue.repository.ServiceQueueRepository;

@RestController
public class WebSocketController {
    private final SimpMessagingTemplate messagingTemplate;
    private final CustomerRepository customerRepository;
    private final ServiceQueueRepository queueRepository;

    public WebSocketController(SimpMessagingTemplate messagingTemplate, CustomerRepository customerRepository, ServiceQueueRepository queueRepository){
        this.messagingTemplate = messagingTemplate;
        this.customerRepository = customerRepository;
        this.queueRepository = queueRepository;
    }

    // Broadcast queue to all connected clients

    public void broadcastQueueUpdate(String serviceType) {
        try {
            // Get current queue
            ServiceQueue queue = queueRepository.findByServiceType(serviceType)
                    .orElse(null);
            
            if (queue != null) {
                List<Customer> customers = queue.getCustomers();
                
                Map<String, Object> update = new HashMap<>();
                update.put("serviceType", serviceType);
                update.put("queueSize", customers.size());
                update.put("customers", customers);
                update.put("timestamp", System.currentTimeMillis());
                
                // Broadcast to topic
                messagingTemplate.convertAndSend(
                    "/topic/queue/" + serviceType, 
                    update
                );
                
                // Broadcast to general topic
                messagingTemplate.convertAndSend(
                    "/topic/queue/all", 
                    update
                );
            }
            
        } catch (Exception e) {
            System.err.println("Error broadcasting queue update: " + e.getMessage());
        }
    }

    // Send ETA update for a specific customer

    public void sendCustomerUpdate(Customer customer) {
        try {
            Map<String, Object> update = new HashMap<>();
            update.put("customerId", customer.getId());
            update.put("tokenNumber", customer.getTokenNumber());
            update.put("position", customer.getPosition());
            update.put("eta", customer.getEta());
            update.put("status", customer.getStatus());
            update.put("timestamp", System.currentTimeMillis());
            
            messagingTemplate.convertAndSend(
                "/topic/customer/" + customer.getAccessKey(), 
                update
            );
        } catch (Exception e) {
            System.err.println("Error sending customer update: " + e.getMessage());
        }
    }

    // Handle client requesting current queue

    @MessageMapping("/queue/request")
    @SendTo(("/topic/queue/all"))
    public Map<String, Object> requestQueue(String serviceType){
        ServiceQueue queue = queueRepository.findByServiceType(serviceType).orElse(null); 

        Map<String, Object> response = new HashMap<>();

        response.put("serviceType", serviceType);
        response.put("queueSize", queue != null ? queue.getCustomers().size() : 0);
        response.put("customers", queue != null ? queue.getCustomers() : List.of());
        response.put("timestamp", System.currentTimeMillis());

        return response;
    }
}
