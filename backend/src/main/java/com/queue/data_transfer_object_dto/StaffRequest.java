package com.queue.data_transfer_object_dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class StaffRequest {
    
    @NotBlank (message = "Name is required")
    private String name;
    @NotBlank (message = "Email is required")
    @Email(message="Email must be valid")
    private String email;
    @NotBlank (message = "Phone is required")
    private String phone;
    @NotBlank (message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;
    @NotBlank (message = "EmployeeID is required")
    private String employeeId;
    private String department;
    private Integer counterNumber;

    // Default constructor
    public StaffRequest() {}

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Integer getCounterNumber() {
        return counterNumber;
    }

    public void setCounterNumber(Integer counterNumber) {
        this.counterNumber = counterNumber;
    }
}
