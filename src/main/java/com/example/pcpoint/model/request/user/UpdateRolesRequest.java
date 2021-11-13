package com.example.pcpoint.model.request.user;


import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

public class UpdateRolesRequest {

    @Positive
    private Long id;

    @Valid

    private List<String> roles;

    public Long getId() {
        return id;
    }

    public UpdateRolesRequest setId(Long id) {
        this.id = id;
        return this;
    }

    public List<String> getRoles() {
        return roles;
    }

    public UpdateRolesRequest setRoles(List<String> roles) {
        this.roles = roles;
        return this;
    }
}
