package com.example.pcpoint.controller;

import com.example.pcpoint.model.entity.user.UserEntity;
import com.example.pcpoint.model.request.user.LoginRequest;
import com.example.pcpoint.model.request.user.RegisterRequest;
import com.example.pcpoint.model.request.user.UpdateRolesRequest;
import com.example.pcpoint.model.response.JwtResponse;
import com.example.pcpoint.model.response.MessageResponse;
import com.example.pcpoint.model.service.user.UpdateRolesServiceModel;
import com.example.pcpoint.model.service.user.UserLoginServiceModel;
import com.example.pcpoint.model.service.user.UserRegisterServiceModel;
import com.example.pcpoint.service.user.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class UserController {


    private final UserService userService;


    private final ModelMapper modelMapper;

    public UserController( UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest,
                                              BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid login request data!"));
        }

        UserLoginServiceModel userLoginServiceModel =
                modelMapper.map(loginRequest, UserLoginServiceModel.class);

        JwtResponse response = userService.loginUser(userLoginServiceModel);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/update_roles")
    public ResponseEntity<?> updateRoles(@Valid @RequestBody UpdateRolesRequest updateRolesRequest,
                                         BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid role update request data!"));
        }

        UpdateRolesServiceModel updateRolesServiceModel =
                modelMapper.map(updateRolesRequest, UpdateRolesServiceModel.class);

        UserEntity updated = userService.updateRoles(updateRolesServiceModel);

        return ResponseEntity.ok(updated);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest,
                                          BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid register request data!"));
        }


        if (userService.existsByUsername(registerRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userService.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Passwords do not match!"));
        }

        UserRegisterServiceModel userRegisterServiceModel =
                modelMapper.map(registerRequest, UserRegisterServiceModel.class);


        userService.registerUser(userRegisterServiceModel);


        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

}
