package com.example.pcpoint.repository.product;

import com.example.pcpoint.model.entity.product.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    Optional<ProductEntity> findProductById(Long id);

    Optional<ProductEntity> findProductByName(String name);

    @Transactional
    @Modifying
    void deleteProductEntityById(Long id);

}
