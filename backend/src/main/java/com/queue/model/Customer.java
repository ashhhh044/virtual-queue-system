package com.queue.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
public class Customer extends User {

    private String tokenNumber;
    private String accessKey;
    private String priority; //emergency, high, normal
    private Boolean isElderly;
    private Boolean hasDisability;
    private Boolean isEmergency;
    private Integer position;
    private Integer eta;
    private String status; //waiting, called, serviced, no-show
    private LocalDateTime joinedAt;
    private LocalDateTime calledAt;
    private LocalDateTime servedAt;

    //constructors

    public Customer(){
        super();
        this.status = "waiting";
        this.priority = "normal";
        this.isElderly = false;
        this.hasDisability = false;
        this.isEmergency = false;
        this.joinedAt = LocalDateTime.now();
    }

    public Customer(String name, String email, String phone) {
        super(name, email, phone, "CUSTOMER");
        this.status = "waiting";
        this.priority = "normal";
        this.isElderly = false;
        this.hasDisability = false;
        this.isEmergency = false;
        this.joinedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public String getTokenNumber() {
        return tokenNumber;
    }
    
    public void setTokenNumber(String tokenNumber) {
        this.tokenNumber = tokenNumber;
    }
    
    public String getAccessKey() {
        return accessKey;
    }
    
    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }
    
    public String getPriority() {
        return priority;
    }
    
    public void setPriority(String priority) {
        this.priority = priority;
    }
    
    public Boolean getIsElderly() {
        return isElderly;
    }
    
    public void setIsElderly(Boolean isElderly) {
        this.isElderly = isElderly;
    }
    
    public Boolean getHasDisability() {
        return hasDisability;
    }
    
    public void setHasDisability(Boolean hasDisability) {
        this.hasDisability = hasDisability;
    }
    
    public Boolean getIsEmergency() {
        return isEmergency;
    }
    
    public void setIsEmergency(Boolean isEmergency) {
        this.isEmergency = isEmergency;
    }
    
    public Integer getPosition() {
        return position;
    }
    
    public void setPosition(Integer position) {
        this.position = position;
    }
    
    public Integer getEta() {
        return eta;
    }
    
    public void setEta(Integer eta) {
        this.eta = eta;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }
    
    public void setJoinedAt(LocalDateTime joinedAt) {
        this.joinedAt = joinedAt;
    }
    
    public LocalDateTime getCalledAt() {
        return calledAt;
    }
    
    public void setCalledAt(LocalDateTime calledAt) {
        this.calledAt = calledAt;
    }
    
    public LocalDateTime getServedAt() {
        return servedAt;
    }
    
    public void setServedAt(LocalDateTime servedAt) {
        this.servedAt = servedAt;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return getId() != null && getId().equals(customer.getId());
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : System.identityHashCode(this);
    }

    @Override
    public String toString() {
        return "Customer{" +
                "tokenNumber='" + tokenNumber + '\'' +
                ", priority='" + priority + '\'' +
                ", position=" + position +
                ", status='" + status + '\'' +
                "} " + super.toString();
    }

}
