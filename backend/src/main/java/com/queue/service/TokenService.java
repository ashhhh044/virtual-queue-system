package com.queue.service;

import java.util.concurrent.atomic.AtomicInteger;

public class TokenService {
    
    private final AtomicInteger tokenCounter = new AtomicInteger();

    // generate next token number
    public String generateToken(){
        int number = tokenCounter.getAndIncrement();
        return String.format("T%03d", number);
    }

    // reset token counter for next day
    public void resetTokenCounter(){
        tokenCounter.set(1);
    }
}
