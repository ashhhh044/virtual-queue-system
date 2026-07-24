package com.queue.config;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{
    private final JwtUtil jwtUtil;
    public JwtAuthenticationFilter(JwtUtil jwtUtil){
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException{

        String authHeader = request.getHeader("Authorization");
        // Skip authentication for public endpoints
        String path = request.getRequestURI();

        if (path.startsWith("/api/auth/login") || 
            path.startsWith("/api/customer/join") ||
            path.startsWith("/api/customer/status") ||
            path.startsWith("/api/test/") ||
            path.startsWith("/ws") ||
            path.startsWith("/topic") ||
            path.startsWith("/app") ||
            path.startsWith("/websocket-test") ||
            path.startsWith("/favicon.ico")) {
            filterChain.doFilter(request, response);
            return;
        }
        
        // Check token for protected endpoints

        if(authHeader != null && authHeader.startsWith("Bearer ")){
            String token = authHeader.substring(7);

            try {
                if(jwtUtil.validateToken(token)){
                    String email = jwtUtil.extractUsername(token);
                    String role = jwtUtil.extractRole(token);

                    // Set attributes for use later

                    request.setAttribute("email", email);
                    request.setAttribute("role", role);

                    // Allow access
                    filterChain.doFilter(request, response);


                    return;
                }
            } catch (Exception e) {
                // TODO: handle exception
            }

        }
        // authentication failed
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"success\":false,\"message\":\"Unauthorized - Please Login\"}");
    }
}
