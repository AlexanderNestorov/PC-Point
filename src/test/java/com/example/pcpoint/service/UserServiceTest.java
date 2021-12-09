package com.example.pcpoint.service;

import com.example.pcpoint.exception.ItemNotFoundException;
import com.example.pcpoint.model.entity.review.ReviewEntity;
import com.example.pcpoint.model.entity.user.UserEntity;
import com.example.pcpoint.model.service.review.ReviewAddServiceModel;
import com.example.pcpoint.model.service.user.UserRegisterServiceModel;
import com.example.pcpoint.repository.user.UserRepository;
import com.example.pcpoint.repository.user.UserRoleRepository;
import com.example.pcpoint.service.user.UserService;
import org.h2.engine.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)

public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    UserEntity userEntity1, userEntity2;


    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();


        UserEntity testEntity1 = new UserEntity();
        testEntity1
                .setUsername("test1")
                .setPassword("1234")
                .setEmail("test@mail.com")
                .setRoles(Set.of(userRoleRepository.findById(1L).orElseThrow(
                        () -> new ItemNotFoundException("Role not found")
                ), userRoleRepository.findById(2L).orElseThrow(
                        () -> new ItemNotFoundException("Role not found"))));

        UserEntity testEntity2 = new UserEntity();
        testEntity2
                .setUsername("test2")
                .setPassword("1234")
                .setEmail("test2@mail.com")
                .setRoles(Set.of( userRoleRepository.findById(2L).orElseThrow(
                        () -> new ItemNotFoundException("Role not found"))));

        userEntity1 = userRepository.save(testEntity1);
        userEntity2 = userRepository.save(testEntity2);

    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Testing if testing components load successfully")
    public void contextLoads() {
        assertThat(userService).isNotNull();
        assertThat(userRepository).isNotNull();
        assertThat(userRoleRepository).isNotNull();
    }

    @Test
    @DisplayName("Testing if register method works correctly")
    public void testRegister() {
        UserRegisterServiceModel userRegisterServiceModel = new UserRegisterServiceModel();


        userRegisterServiceModel
                .setEmail("new@email.com")
                .setUsername("new")
                .setPassword("1234")
                .setRoles(List.of("ROLE_USER"));


        userService.registerUser(userRegisterServiceModel);



        assertThat(userService.findByUsername("new")).isNotNull();
    }
}
