package com.queue.data_transfer_object_dto;


public class ServiceRequest {
    
    private String name;
    private String description;
    private Integer estimatedDuration;
    private Boolean isActive;

    // Default Constructor
    public ServiceRequest(){}

    // Getter and Setter

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public Integer getEstimatedDuration(){
        return estimatedDuration;
    }

    public void setEstimatedDuration(Integer estimatedDuration){
        this.estimatedDuration = estimatedDuration;
    }

    public Boolean getIsActive(){
        return isActive;
    }

    public void setIsActive(Boolean isActive){
        this.isActive = isActive;
    }
}
