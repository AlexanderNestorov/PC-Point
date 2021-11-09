package com.example.pcpoint.repository.product;

import com.example.pcpoint.model.entity.product.ProductTypeEntity;
import com.example.pcpoint.model.enums.ProductTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductTypeRepository extends JpaRepository<ProductTypeEntity, Long> {

    Optional<ProductTypeEntity> findByType(ProductTypeEnum name);
}
