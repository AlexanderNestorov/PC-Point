package com.example.pcpoint.service.user;

import com.example.pcpoint.model.entity.user.UserEntity;
import com.example.pcpoint.model.entity.user.UserRoleEntity;
import com.example.pcpoint.model.enums.UserRoleEnum;
import com.example.pcpoint.model.service.UserRegisterServiceModel;
import com.example.pcpoint.repository.UserRepository;
import com.example.pcpoint.repository.UserRoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final UserRoleRepository userRoleRepository;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder encoder, UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.userRoleRepository = userRoleRepository;
    }


    @Override
    public void registerUser(UserRegisterServiceModel userRegisterServiceModel) {
        // Create new user's account
        UserEntity user = new UserEntity();
        String strRoles = userRegisterServiceModel.getRole();

        user.setUsername(userRegisterServiceModel.getUsername())
                .setEmail(userRegisterServiceModel.getEmail())
                .setPassword(encoder.encode(userRegisterServiceModel.getPassword()));


        Set<UserRoleEntity> roles = new HashSet<>();

        if (strRoles == null) {
            UserRoleEntity userRole = userRoleRepository.findByRole(UserRoleEnum.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
                if ("admin".equals(strRoles)) {
                    UserRoleEntity adminRole = userRoleRepository.findByRole(UserRoleEnum.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(adminRole);
                } else {
                    UserRoleEntity userRole = userRoleRepository.findByRole(UserRoleEnum.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(userRole);
                }
        }

        user.setRoles(roles);
        userRepository.save(user);
    }

    @Override
    public UserEntity findByUsername(String username) {
        return this.userRepository.findByUsername(username).orElse(null);
    }

    @Override
    public Boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
