package com.queue.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;

@Entity
public class Admin extends User{

    @Column(nullable = false)  // Admin MUST have password
    private String password;
    
    @ElementCollection
    private List<String> permissions = new ArrayList<>(); 

    public Admin(){
        super();
    }

    public Admin(String name, String email, String phone){
        super(name, email, phone, "ADMIN");
        // default permissions
        this.permissions.add("MANAGE_SERVICES");
        this.permissions.add("MANAGE_STAFF");
        this.permissions.add("VIEW_ANALYTICS");
    }

    // Getters and Setters

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }
    
    public List<String> getPermissions(){
        return permissions;
    }

    public void setPermissions(List<String> permissions){
        this.permissions = permissions;
    }

    public void addPermission(String permission){
        this.permissions.add(permission);
    }

    public void removePermission(String permission){
        this.permissions.remove(permission);
    }

    @Override
    public String toString(){
        return "Admin{" + 
                "permissions=" + permissions +
                "} " + super.toString();
    }
}
