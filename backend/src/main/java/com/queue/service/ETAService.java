package com.queue.service;

import java.util.List;

import com.queue.model.ServiceQueue;
import com.queue.repository.ServiceQueueRepository;

public class ETAService {
    
    private final ServiceQueueRepository queueRepository;

    public ETAService(ServiceQueueRepository queueRepository){
        this.queueRepository = queueRepository;
    }

    // get eta for customer at their position

    public int getETAForCustomer(String serviceType, int position){
        ServiceQueue queue = queueRepository.findByServiceType(serviceType).orElseThrow(() -> new RuntimeException("Queue not found"));

        return queue.calculateEta(position);
    }
    // update average service time based on history
    
    public void updateAverageServiceTime(String serviceType, double actualTime){
        ServiceQueue queue = queueRepository.findByServiceType(serviceType).orElseThrow(()->new RuntimeException("Queue not found"));

        // add to history
        queue.getServiceHistory().add(actualTime);

        // calculate new average
        List<Double> history = queue.getServiceHistory();
        int limit = Math.min(20, history.size());
        double sum = 0;

        for(int i = history.size()-limit; i < history.size(); i++){
            sum += history.get(i);
        }

        double newAvg = sum / limit;
        queue.setAvgServiceTime(newAvg);

        queueRepository.save(queue);
    }

}

