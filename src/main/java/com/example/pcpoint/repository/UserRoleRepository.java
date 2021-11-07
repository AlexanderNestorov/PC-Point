package com.example.pcpoint.repository;

import com.example.pcpoint.model.entity.user.UserRoleEntity;
import com.example.pcpoint.model.enums.UserRoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRoleEntity, Long> {
    Optional<UserRoleEntity> findByRole(UserRoleEnum name);
}
