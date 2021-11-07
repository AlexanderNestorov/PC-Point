package com.example.pcpoint.service.user;


import com.example.pcpoint.model.entity.user.UserEntity;
import com.example.pcpoint.model.service.UserRegisterServiceModel;


public interface UserService {

    void registerUser(UserRegisterServiceModel userRegisterServiceModel);

    UserEntity findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}
