package com.queue.config;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class RateLimitFilter extends OncePerRequestFilter {

    private static final int MAX_REQUESTS = 5;
    private static final long WINDOW_MS = 60_000; //1 minute

    private static class Window{
        volatile long windowStart = System.currentTimeMillis();
        final AtomicInteger count = new AtomicInteger(0);
    }

    private final ConcurrentHashMap<String, Window> buckets = new ConcurrentHashMap<>();
    
    @Override 
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException{

        String path = request.getRequestURI();
        boolean limited = path.equals("/api/customer/join") || path.equals("/api/auth/login");

        if(limited){
            String key = path + ":" + request.getRemoteAddr();
            Window window = buckets.computeIfAbsent(key, k-> new Window());

            synchronized(window){
                long now = System.currentTimeMillis();
                if(now - window.windowStart > WINDOW_MS){
                    window.windowStart = now;
                    window.count.set(0);
                }
                if(window.count.incrementAndGet() > MAX_REQUESTS){
                    response.setStatus(429); //too many requests
                    response.setContentType("application/json");
                    response.getWriter().write(
                        "{\"success\": false, \"message\": \"Too many requests - please wait a minute and try again\"}"
                    );
                    return;
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
