package com.example.pcpoint.model.service.user;

import java.util.List;

public class UpdateRolesServiceModel {

    private Long id;

    private List<String> roles;

    public Long getId() {
        return id;
    }

    public UpdateRolesServiceModel setId(Long id) {
        this.id = id;
        return this;
    }

    public List<String> getRoles() {
        return roles;
    }

    public UpdateRolesServiceModel setRoles(List<String> roles) {
        this.roles = roles;
        return this;
    }
}
