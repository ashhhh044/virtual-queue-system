package com.queue.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.queue.controllers.WebSocketController;
import com.queue.model.Customer;
import com.queue.model.ServiceQueue;
import com.queue.repository.CustomerRepository;
import com.queue.repository.ServiceQueueRepository;

@Service
public class QueueService {
    
    private final CustomerRepository customerRepository;
    private final ServiceQueueRepository queueRepository;
    private final WebSocketController webSocketController;

    public QueueService(CustomerRepository customerRepository, ServiceQueueRepository queueRepository, WebSocketController webSocketController){
        this.customerRepository = customerRepository;
        this.queueRepository = queueRepository;
        this.webSocketController = webSocketController;
    }

    // Join a customer to a queue

    @Transactional
    public Customer joinQueue(String customerName, String email, String phone, String serviceType, String priority){

        // check if queue exists, else create it
        
        ServiceQueue queue = queueRepository.findByServiceType(serviceType).orElseGet(() -> {
            ServiceQueue newQueue = new ServiceQueue(serviceType);
            return queueRepository.save(newQueue);
        });

        // Create new customer

        Customer customer = new Customer();

        customer.setName(customerName);
        customer.setEmail(email);
        customer.setPhone(phone);
        customer.setPriority(priority != null ? priority : "normal");
        customer.setStatus("waiting");
        customer.setJoinedAt(LocalDateTime.now());
         customer.setRole("CUSTOMER");

        // generate token number
        int queueSize = queue.getCustomers().size();
        customer.setTokenNumber(String.format("T%03d", queueSize+1));

        // generate access key for customers to check status
        customer.setAccessKey(generateAccessKey());

        // save the customer
        customer = customerRepository.save(customer);

        // add to queue
        queue.addCustomer(customer);
        queueRepository.save(queue);

        // broadcast queue update
        webSocketController.broadcastQueueUpdate(serviceType);

        // Send customer update
        webSocketController.sendCustomerUpdate(customer);

        return customer;
    }

    // get customer status by access key 

    public Customer getCustomerStatus(String accessKey){
        return customerRepository.findByAccessKey(accessKey).orElseThrow(() -> new RuntimeException("Customer Not Found!"));
    }

    // get current queue for service

    public List<Customer> getCurrentQueue(String serviceType){
        ServiceQueue queue = queueRepository.findByServiceType(serviceType).orElseThrow(() -> new RuntimeException("Queue not found!"));
        return queue.getCustomers();
    }

    // call next customer in queue

    @Transactional
    public Customer getNextCustomer(String serviceType){
        ServiceQueue queue = queueRepository.findByServiceType(serviceType).orElseThrow(()->new RuntimeException("Queue not found!"));
        Customer next = queue.getNextCustomer();

        if(next != null){
            customerRepository.save(next);
            queueRepository.save(queue);
        }

        // update positions for other customers
         for (int i = 0; i < queue.getCustomers().size(); i++) {
            Customer c = queue.getCustomers().get(i);
            c.setPosition(i + 1);
            c.setEta(queue.calculateEta(i + 1));
            customerRepository.save(c);

            queueRepository.save(queue);

            webSocketController.broadcastQueueUpdate(serviceType);
        }
        
        return next;
    }

    // complete service for a customer

    @Transactional
    public Customer completeService(Long customerId){
        Customer customer = customerRepository.findById(customerId).orElseThrow(()-> new RuntimeException("Customer not found"));

        customer.setStatus("served");
        customer.setServedAt(LocalDateTime.now());

        Customer saved = customerRepository.save(customer);

        // find queue and broadcast update
        ServiceQueue queue = queueRepository.findAll().stream().filter(q -> q.getCustomers().stream().anyMatch(c -> c.getId().equals(customerId))).findFirst().orElse(null);

        if(queue != null){
            webSocketController.broadcastQueueUpdate(queue.getServiceType());
        }
        return saved;
    }

    // cancel customer's spot from a queue

    @Transactional
    public boolean cancelQueueSpot(String accessKey) {

        Customer customer = customerRepository.findByAccessKey(accessKey)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        if (!"waiting".equals(customer.getStatus())) {
            throw new RuntimeException("Cannot cancel - customer is already called or served");
        }

        // FIND the queue this customer belongs to by matching accessKey
        ServiceQueue queue = queueRepository.findAll().stream()
            .filter(q -> q.findCustomerByAccessKey(accessKey) != null)
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Customer not found in any queue"));

        String serviceType = queue.getServiceType();


        // REMOVE customer from queue (use id match to be robust)
        queue.getCustomers().removeIf(c -> c.getId() != null && c.getId().equals(customer.getId()));

        // UPDATE positions for remaining customers
        for (int i = 0; i < queue.getCustomers().size(); i++) {
            Customer c = queue.getCustomers().get(i);
            c.setPosition(i + 1);
            c.setEta(queue.calculateEta(i + 1));
            customerRepository.save(c);
        }

        // UPDATE customer status
        customer.setStatus("no-show");
        customer.setPosition(null);
        customer.setEta(null);
        customerRepository.save(customer);

        // SAVE the updated queue
        queueRepository.save(queue);

        webSocketController.broadcastQueueUpdate(serviceType);

        System.out.println("Customer " + customer.getName() + " cancelled and removed from queue");

        return true;
    }

    private String generateAccessKey(){
        return UUID.randomUUID().toString().substring(0,8).toUpperCase();
    }
}
