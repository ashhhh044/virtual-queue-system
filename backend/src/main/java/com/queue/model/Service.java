package com.queue.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "services")

public class Service {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    @Column(name = "estimated_duration")
    private Integer estimatedDuration;
    
    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public Service(){
        this.createdAt = LocalDateTime.now();
        this.isActive = true;
    }

    public Service(String name, String description, Integer estimatedDuration){
        this.name = name;
        this.description = description;
        this.estimatedDuration = estimatedDuration;
        this.isActive = true;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId(){
        return Id;
    }

    public void setId(Long Id){
        this.Id = Id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Integer getEstimatedDuration() {
        return estimatedDuration;
    }
    
    public void setEstimatedDuration(Integer estimatedDuration) {
        this.estimatedDuration = estimatedDuration;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public String toString() {
        return "Service{" +
                "id=" + Id +
                ", name='" + name + '\'' +
                ", estimatedDuration=" + estimatedDuration +
                ", isActive=" + isActive +
                '}';
    }
}
