package com.example.pcpoint.repository.user;

import com.example.pcpoint.model.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findById(Long id);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

}
