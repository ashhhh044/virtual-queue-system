package com.queue.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class Staff extends User{
    
    @Column(nullable = false)  // ✅ Staff MUST have password
    private String password;

    private String employeeId;
    private String department;
    private Integer counterNumber;
    

    public Staff(){
        super();
    }

    public Staff(String name, String email, String phone, String employeeId, String department){
        super(name, email, phone, "STAFF");
        this.employeeId = employeeId;
        this.department = department;
        this.counterNumber = 1;
    }

    // Getter and Setter

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getEmployeeId(){
        return employeeId;
    }

    public void setEmployeeId(String employeeId){
        this.employeeId = employeeId;
    }

    public String getDepartment(){
        return department;
    }

    public void setDepartment(String department){
        this.department = department;
    }

    public Integer getCounterNumber(){
        return counterNumber;
    }

    public void setCounterNumber(Integer counterNumber){
        this.counterNumber = counterNumber;
    }

    @Override
    public String toString(){
        return "Staff{" + 
                "employeeId='" + employeeId + "/'" +
                ", department='" + department + "/'" +
                ", counterNumber='" + counterNumber +
                "} " + super.toString();

    }
}
