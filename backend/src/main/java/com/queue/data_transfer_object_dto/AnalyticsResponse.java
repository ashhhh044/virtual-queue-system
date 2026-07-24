package com.queue.data_transfer_object_dto;

import java.util.HashMap;
import java.util.Map;

public class AnalyticsResponse {
    private Long totalCustomers;
    private Long waitingCustomers;
    private Long servedToday;
    private Map<String, Long> serviceDistribution = new HashMap<>();
    private Double averageWaitTime;

    // Default constructor
    public AnalyticsResponse() {}

    // Getters and Setters
    public Long getTotalCustomers() {
        return totalCustomers;
    }

    public void setTotalCustomers(Long totalCustomers) {
        this.totalCustomers = totalCustomers;
    }

    public Long getWaitingCustomers() {
        return waitingCustomers;
    }

    public void setWaitingCustomers(Long waitingCustomers) {
        this.waitingCustomers = waitingCustomers;
    }

    public Long getServedToday() {
        return servedToday;
    }

    public void setServedToday(Long servedToday) {
        this.servedToday = servedToday;
    }

    public Map<String, Long> getServiceDistribution() {
        return serviceDistribution;
    }

    public void setServiceDistribution(Map<String, Long> serviceDistribution) {
        this.serviceDistribution = serviceDistribution;
    }

    public Double getAverageWaitTime() {
        return averageWaitTime;
    }

    public void setAverageWaitTime(Double averageWaitTime) {
        this.averageWaitTime = averageWaitTime;
    }
}