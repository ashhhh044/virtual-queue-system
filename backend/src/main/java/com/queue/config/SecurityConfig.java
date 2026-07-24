package com.queue.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter){
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
        .csrf(csrf -> csrf.disable())
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .authorizeHttpRequests(authz -> authz
            // Public Endpoints
            .requestMatchers("/api/auth/login").permitAll()
            .requestMatchers("/api/customer/join").permitAll()
            .requestMatchers("/api/customer/status/**").permitAll()
            .requestMatchers("/api/test/**").permitAll()
            .requestMatchers("/", "/index.html", "/**/*.html", "/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll()

            // Websocket endpoints
            .requestMatchers("/ws/**").permitAll()
            .requestMatchers("/ws").permitAll()
            .requestMatchers("/topic/**").permitAll()
            .requestMatchers("/app/**").permitAll()

            // Staff Endpoints - require STAFF role
            .requestMatchers("/api/staff/**").hasAnyRole("STAFF", "ADMIN")

            // Admin Endpoints - require ADMIN role
            .requestMatchers("/api/admin/**").hasRole("ADMIN")

            // All other roles erquire authentication
            .anyRequest().authenticated()
        )
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )

        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
