package com.example.pcpoint.repository;

import com.example.pcpoint.exception.ItemNotFoundException;
import com.example.pcpoint.model.entity.user.UserEntity;
import com.example.pcpoint.repository.user.UserRepository;
import com.example.pcpoint.repository.user.UserRoleRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    UserEntity userEntity1, userEntity2;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();

        UserEntity testEntity1 = new UserEntity();

        testEntity1.setUsername("test1")
                .setEmail("test@email.com")
                .setPassword("test")
                .setRoles(Set.of(userRoleRepository.findById(1L).orElseThrow(
                        () -> new ItemNotFoundException("Role not found")
                ), userRoleRepository.findById(2L).orElseThrow(
                        () -> new ItemNotFoundException("Role not found"))));


        UserEntity testEntity2 = new UserEntity();

        testEntity2.setUsername("test2")
                .setEmail("test2@email.com")
                .setPassword("test2")
                .setRoles(Set.of(userRoleRepository.findById(2L).orElseThrow(
                        () -> new ItemNotFoundException("Role not found")
                )));

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
        assertThat(userRoleRepository).isNotNull();
        assertThat(userRepository).isNotNull();
    }

    @Test
    @DisplayName("Testing if get method returns correct amount of users")
    public void testGetAllMethod() {
        assertThat(userRepository.findAll().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Testing if add method works correctly")
    public void testAdd() {
        assertThat(userRepository.findAll().size()).isEqualTo(2);
        assertThat(userRepository.findById(userEntity1.getId()).get().getId()).isEqualTo(3L);
    }

    @Test
    @DisplayName("Testing if find by id method works correctly")
    public void testFindById() {
        UserEntity test = userRepository.findById(userEntity1.getId()).orElseThrow(
                () -> new ItemNotFoundException("User not found")
        );
        assertThat(test).isNotNull();
        assertThat(test.getId()).isEqualTo(userEntity1.getId());
    }

    @Test
    @DisplayName("Testing if find by username method works correctly")
    public void testFindByUsername() {
        UserEntity test = userRepository.findByUsername(userEntity1.getUsername()).orElseThrow(
                () -> new ItemNotFoundException("User not found")
        );
        assertThat(test).isNotNull();
        assertThat(test.getUsername()).isEqualTo(userEntity1.getUsername());
        assertThat(test.getId()).isEqualTo(userEntity1.getId());
    }

    @Test
    @DisplayName("Testing if delete by id method works correctly")
    public void testDeleteById() {
        UserEntity test = userRepository.findById(userEntity2.getId()).orElse(null);
        assertThat(test).isNotNull();

        userRepository.deleteById(test.getId());

        assertThat(userRoleRepository.count()).isEqualTo(2);
        assertThat(userRepository.findById(userEntity2.getId()).orElse(null)).isNull();
    }

    @Test
    @DisplayName("Testing if exists by username method works correctly")
    public void testExistsByUsername() {
            assertThat(userRepository.existsByUsername(userEntity1.getUsername())).isTrue();
            assertThat(userRepository.existsByUsername(userEntity2.getUsername())).isTrue();
            assertThat(userRepository.existsByUsername("test3")).isFalse();
    }

    @Test
    @DisplayName("Testing if exists by email method works correctly")
    public void testExistsByEmail() {
        assertThat(userRepository.existsByEmail(userEntity1.getEmail())).isTrue();
        assertThat(userRepository.existsByEmail(userEntity2.getEmail())).isTrue();
        assertThat(userRepository.existsByEmail("test3@email.com")).isFalse();
    }
}
