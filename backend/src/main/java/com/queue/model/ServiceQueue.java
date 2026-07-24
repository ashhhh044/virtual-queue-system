package com.queue.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "service_queue")
public class ServiceQueue {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "service_type")
    private String serviceType;
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "queue_id")
    @OrderColumn(name = "queue_position_index")
    private List<Customer> customers = new ArrayList<>();
    
    @Column(name = "active_counters")
    private Integer activeCounters = 1;
    
    @Column(name = "avg_service_time")
    private Double avgServiceTime = 5.0;
    
    private String status = "ACTIVE";  // ACTIVE, CLOSED
    
    @ElementCollection
    private List<Double> serviceHistory = new ArrayList<>();
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    public ServiceQueue() {
        this.createdAt = LocalDateTime.now();
        this.status = "ACTIVE";
    }
    
    public ServiceQueue(String serviceType) {
        this.serviceType = serviceType;
        this.activeCounters = 1;
        this.avgServiceTime = 5.0;
        this.status = "ACTIVE";
        this.createdAt = LocalDateTime.now();
        this.serviceHistory = new ArrayList<>();
        this.customers = new ArrayList<>();
    }

    //OOP Methods
    public void addCustomer(Customer customer){
        // Emergency > High > Normal
        if("emergency".equalsIgnoreCase(customer.getPriority())){
            // add to beginning of queue
            customers.add(0, customer);
            System.out.println("Emergency customer added at position 0");
        }
        // add after all emergency customers
        else if("high".equalsIgnoreCase(customer.getPriority())){
            int insertIndex = getLastEmergencyIndex() + 1;
            customers.add(insertIndex, customer);
            System.out.println("High customer added at position: " + insertIndex);
        }
        // normal priority - add to end
        else{
            customers.add(customer);
            System.out.println("Normal customer added at position: "+ (customers.size()-1));
        }

        updateAllPositions();

        // position and ETA 
        // customer.setPosition(customers.indexOf(customer)+1);
        // customer.setEta(calculateEta(customer.getPosition()));
        // customer.setJoinedAt(LocalDateTime.now());
        // customer.setStatus("waiting");
    }

    public Customer getNextCustomer(){
        if(customers.isEmpty()){
            return null;
        }

        Customer next = customers.remove(0);
        next.setStatus("called");
        next.setCalledAt(LocalDateTime.now());
        return next;
    }

    public int calculateEta(int position){
        double avg = getMovingAverage();
        return (int) Math.ceil((position/ (double) activeCounters)* avg);
    }

    private double getMovingAverage(){
            if(serviceHistory.isEmpty()){
                return 5.0; //5 is set for default estiamte time
            }
        double sum = 0;
        
        int limit = Math.min(20, serviceHistory.size()); // capping calcualtion at 20 entries tops

        for(int i = serviceHistory.size() - limit; i < serviceHistory.size(); i++){ //moving average
            sum += serviceHistory.get(i);
        }
        return sum/limit;
    }

    private int getLastEmergencyIndex(){
        int lastEmergencyIndex = -1;

        for (int i = 0; i < customers.size();i++){
            if ("emergency".equalsIgnoreCase(customers.get(i).getPriority())) {
                lastEmergencyIndex = i;
            }
        }
        // High priority customers go after ALL emergency customers
        return lastEmergencyIndex;
    }

    // Update positions for customers in the queue
    private void updateAllPositions() {
        for (int i = 0; i < customers.size(); i++) {
            Customer c = customers.get(i);
            c.setPosition(i + 1);
            c.setEta(calculateEta(i + 1));
            System.out.println("📊 Position " + (i + 1) + ": " + c.getName() + " (" + c.getPriority() + ")");
        }
    }

    public void reorderByPriority(){
        List<Customer> emergency = new ArrayList<>();
        List<Customer> high = new ArrayList<>();
        List<Customer> normal = new ArrayList<>();

        // separating customers based on priority
        for(Customer c : customers){
            if("emergency".equalsIgnoreCase(c.getPriority())){
                emergency.add(c);
            }
            else if("high".equalsIgnoreCase(c.getPriority())){
                high.add(c);
            }
            else{
                normal.add(c);
            }
        }
        // queue in correct order
        customers.clear();
        customers.addAll(emergency);
        customers.addAll(high);
        customers.addAll(normal);

    }

    //  Remove a customer from the queue
    public boolean removeCustomer(Customer customer) {
        if (customers.remove(customer)) {
            updateAllPositions();
            System.out.println("🗑️ Customer " + customer.getName() + " removed from queue");
            return true;
        }
        return false;
    }

    //  Find a customer in the queue by access key
    public Customer findCustomerByAccessKey(String accessKey) {
        for (Customer c : customers) {
            if (accessKey.equals(c.getAccessKey())) {
                return c;
            }
        }
        return null;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getServiceType() {
        return serviceType;
    }
    
    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }
    
    public List<Customer> getCustomers() {
        return customers;
    }
    
    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }
    
    public Integer getActiveCounters() {
        return activeCounters;
    }
    
    public void setActiveCounters(Integer activeCounters) {
        this.activeCounters = activeCounters;
    }
    
    public Double getAvgServiceTime() {
        return avgServiceTime;
    }
    
    public void setAvgServiceTime(Double avgServiceTime) {
        this.avgServiceTime = avgServiceTime;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public List<Double> getServiceHistory() {
        return serviceHistory;
    }
    
    public void setServiceHistory(List<Double> serviceHistory) {
        this.serviceHistory = serviceHistory;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public String toString() {
        return "ServiceQueue{" +
                "id=" + id +
                ", serviceType='" + serviceType + '\'' +
                ", activeCounters=" + activeCounters +
                ", customers=" + customers.size() +
                ", avgServiceTime=" + avgServiceTime +
                ", status='" + status + '\'' +
                '}';
    }
}
