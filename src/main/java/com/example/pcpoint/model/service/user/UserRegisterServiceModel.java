package com.example.pcpoint.model.service.user;

import java.util.List;

public class UserRegisterServiceModel {

    private String username;
    private String password;
    private String email;
    private List<String> roles;

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

    public List<String> getRoles() {
        return roles;
    }

    public UserRegisterServiceModel setRoles(List<String> roles) {
        this.roles = roles;
        return this;
    }
}
