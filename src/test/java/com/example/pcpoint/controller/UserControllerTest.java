package com.example.pcpoint.controller;

import com.example.pcpoint.exception.ItemNotFoundException;
import com.example.pcpoint.model.entity.user.UserEntity;
import com.example.pcpoint.model.entity.user.UserRoleEntity;
import com.example.pcpoint.model.request.user.LoginRequest;
import com.example.pcpoint.model.request.user.RegisterRequest;
import com.example.pcpoint.model.service.user.UserRegisterServiceModel;
import com.example.pcpoint.repository.user.UserRepository;
import com.example.pcpoint.repository.user.UserRoleRepository;
import com.example.pcpoint.service.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private UserService userService;


    private UserEntity userEntity1, userEntity2;

    private UserRoleEntity userRoleEntity1, userRoleEntity2;

    @BeforeEach
    public void setUp(){

        userRepository.deleteAll();

        userRoleEntity1 = userRoleRepository.findById(1L)
                .orElseThrow(() -> new ItemNotFoundException("Role not found"));

        userRoleEntity2 = userRoleRepository.findById(2L)
                .orElseThrow(() -> new ItemNotFoundException("Role not found"));



        UserEntity user1 = new UserEntity();


        user1.setUsername("user1")
                .setPassword("1234")
                .setEmail("user1@gmail.com")
                .setRoles(Set.of(userRoleEntity1,userRoleEntity2));

        userEntity1 = userRepository.save(user1);

        UserEntity user2 = new UserEntity();


        user2.setUsername("user2")
                .setPassword("1234")
                .setEmail("user2@gmail.com")
                .setRoles(Set.of(userRoleEntity2));

        userEntity2 = userRepository.save(user2);

        UserRegisterServiceModel userRegisterServiceModel = new UserRegisterServiceModel();
        userRegisterServiceModel.setUsername("admin");
        userRegisterServiceModel.setPassword("1234");
        userRegisterServiceModel.setEmail("admin@gmail.com");
        userRegisterServiceModel.setRoles(List.of(userRoleEntity1.getRole().name(), userRoleEntity2.getRole().name()));

        UserEntity registerResponseAdmin = userService.registerUser(userRegisterServiceModel);
    }

    @AfterEach
    public void tearDown(){
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Testing if testing components load successfully")
    public void contextLoads(){
        assertThat(userRoleRepository).isNotNull();
        assertThat(mockMvc).isNotNull();
        assertThat(userService).isNotNull();
        assertThat(userRepository).isNotNull();
    }

    @Test
    @DisplayName("Testing register with correct data")
    public void registerUserWithCorrectData() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest();

        registerRequest.setUsername("user3");
        registerRequest.setPassword("1234");
        registerRequest.setConfirmPassword("1234");
        registerRequest.setEmail("user3@gmail.com");
        registerRequest.setRoles(List.of((userRoleEntity1.getRole().name()),userRoleEntity2.getRole().name()));


        ObjectMapper mapper = new ObjectMapper();

        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Testing register with incorrect data")
    public void registerUserWithIncorrectData() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest();

        registerRequest.setUsername("user3");
        registerRequest.setPassword("1234");
        registerRequest.setConfirmPassword("1234");
//        registerRequest.setEmail("user3@gmail.com");
       registerRequest.setRoles(List.of((userRoleEntity1.getRole().name()),userRoleEntity2.getRole().name()));


        ObjectMapper mapper = new ObjectMapper();

        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Testing login with correct data")
    public void loginUserWithCorrectData() throws Exception {
        LoginRequest loginRequest = new LoginRequest();

        loginRequest.setUsername("admin");
        loginRequest.setPassword("1234");


        ObjectMapper mapper = new ObjectMapper();

        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Testing login with incorrect data")
    public void loginUserWithIncorrectData() throws Exception {
        LoginRequest loginRequest = new LoginRequest();

        loginRequest.setUsername("user3_not_existing");
        loginRequest.setPassword("1234");


        ObjectMapper mapper = new ObjectMapper();

        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }
}
