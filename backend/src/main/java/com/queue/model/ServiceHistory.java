package com.queue.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "service_history")
public class ServiceHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "staff_id")
    private Staff staff;

    @Column(name = "service_time")
    private Double serviceTime;

    private String priority;

    @Column(name = "joined_at")
    private LocalDateTime joinedAt;
    
    @Column(name = "served_at")
    private LocalDateTime servedAt;
    
    private String status;  // completed, no-show
    
    public ServiceHistory() {}

    public ServiceHistory(Service service, Customer customer, Staff staff, Double serviceTime, String status){  
        this.service = service;
        this.customer = customer;
        this.staff = staff;
        this.serviceTime = serviceTime;    
        this.status = status;
        this.priority = customer != null ? customer.getPriority() : "normal";
        this.joinedAt = customer != null ? customer.getJoinedAt() : LocalDateTime.now();
        this.servedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() {
        return Id;
    }
    
    public void setId(Long Id) {
        this.Id = Id;
    }
    
    public Service getService() {
        return service;
    }
    
    public void setService(Service service) {
        this.service = service;
    }
    
    public Customer getCustomer() {
        return customer;
    }
    
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    public Staff getStaff() {
        return staff;
    }
    
    public void setStaff(Staff staff) {
        this.staff = staff;
    }
    
    public Double getServiceTime() {
        return serviceTime;
    }
    
    public void setServiceTime(Double serviceTime) {
        this.serviceTime = serviceTime;
    }
    
    public String getPriority() {
        return priority;
    }
    
    public void setPriority(String priority) {
        this.priority = priority;
    }
    
    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }
    
    public void setJoinedAt(LocalDateTime joinedAt) {
        this.joinedAt = joinedAt;
    }
    
    public LocalDateTime getServedAt() {
        return servedAt;
    }
    
    public void setServedAt(LocalDateTime servedAt) {
        this.servedAt = servedAt;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    @Override
    public String toString() {
        return "ServiceHistory{" +
                "Id=" + Id +
                ", serviceTime=" + serviceTime +
                ", priority='" + priority + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
