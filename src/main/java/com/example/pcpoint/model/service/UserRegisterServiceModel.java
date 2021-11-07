package com.example.pcpoint.model.service;

import java.util.Set;

public class UserRegisterServiceModel {

    private String username;
    private String password;
    private String email;
    private String role;

    public String getUsername() {
        return username;
    }

    public UserRegisterServiceModel setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserRegisterServiceModel setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserRegisterServiceModel setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getRole() {
        return role;
    }

    public UserRegisterServiceModel setRole(String role) {
        this.role = role;
        return this;
    }
}
