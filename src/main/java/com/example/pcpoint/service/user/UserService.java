package com.example.pcpoint.service.user;


import com.example.pcpoint.model.entity.user.UserEntity;
import com.example.pcpoint.model.response.JwtResponse;
import com.example.pcpoint.model.service.user.UserLoginServiceModel;
import com.example.pcpoint.model.service.user.UserRegisterServiceModel;


public interface UserService {

    void registerUser(UserRegisterServiceModel userRegisterServiceModel);

    JwtResponse loginUser(UserLoginServiceModel userLoginServiceModel);

    UserEntity findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    void initializeUsersAndRoles();
}
