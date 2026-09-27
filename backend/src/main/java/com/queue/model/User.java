package com.queue.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)

public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    // NOTE: intentionally NOT unique at the DB level. This column is shared
    // by Customer, Staff, and Admin (JOINED inheritance). A unique constraint
    // here would stop a customer from ever joining the queue a second time
    // with the same email. Staff/Admin emails are already checked for
    // uniqueness in AdminService/AuthController before saving.

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String role; //cutomer, admin, staff

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "refresh_token_expiry")
    private LocalDateTime refreshTokenExpiry;

    public User(){
        this.createdAt = LocalDateTime.now();
    }

    public User(String name, String email, String phone, String role){
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getPhone(){
        return phone;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }

    public String getRole(){
        return role;
    }

    public void setRole(String role){
        this.role = role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
    
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public LocalDateTime getRefreshTokenExpiry() {
        return refreshTokenExpiry;
    }
    
    public void setRefreshTokenExpiry(LocalDateTime refreshTokenExpiry) {
        this.refreshTokenExpiry = refreshTokenExpiry;
    }

    @Override
    public String toString(){
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", createdAt=" + createdAt +
                ", refreshToken=" + refreshToken +
                ", refreshTokenExpiry=" + refreshTokenExpiry +
                '}';
    }

}


