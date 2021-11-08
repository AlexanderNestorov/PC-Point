package com.example.pcpoint.service.user;

import com.example.pcpoint.model.entity.user.UserEntity;
import com.example.pcpoint.model.entity.user.UserRoleEntity;
import com.example.pcpoint.model.enums.UserRoleEnum;
import com.example.pcpoint.model.response.JwtResponse;
import com.example.pcpoint.model.service.UserLoginServiceModel;
import com.example.pcpoint.model.service.UserRegisterServiceModel;
import com.example.pcpoint.repository.UserRepository;
import com.example.pcpoint.repository.UserRoleRepository;
import com.example.pcpoint.security.jwt.JwtUtils;
import com.example.pcpoint.security.user.UserDetailsImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final UserRoleRepository userRoleRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder encoder, UserRoleRepository userRoleRepository, AuthenticationManager authenticationManager, JwtUtils jwtUtils, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.userRoleRepository = userRoleRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
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
    public JwtResponse loginUser(UserLoginServiceModel userLoginServiceModel) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userLoginServiceModel.getUsername(), userLoginServiceModel.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles);
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

    @Override
    public void initializeUsersAndRoles() {
        initializeRoles();
        initializeAdmin();
    }

    private void initializeAdmin() {
        if (userRepository.count() == 0) {
            UserRoleEntity adminRole = userRoleRepository.findByRole(UserRoleEnum.ROLE_ADMIN).orElse(null);

            UserEntity admin = new UserEntity();
            admin
                    .setUsername("admin")
                    .setPassword(passwordEncoder.encode("1234"))
                    .setEmail("admin@email.com")
                    .setRoles(new HashSet<>(List.of(adminRole)));

            userRepository.save(admin);
        }
    }

    private void initializeRoles() {
        if (userRoleRepository.count() == 0) {
            UserRoleEntity adminRole = new UserRoleEntity();
            adminRole.setRole(UserRoleEnum.ROLE_ADMIN);
            userRoleRepository.save(adminRole);

            UserRoleEntity userRole = new UserRoleEntity();
            userRole.setRole(UserRoleEnum.ROLE_USER);
            userRoleRepository.save(userRole);
        }
    }
}
