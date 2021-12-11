package com.example.pcpoint.repository.product;

import com.example.pcpoint.model.entity.product.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    Optional<ProductEntity> findProductById(Long id);

    Optional<ProductEntity> findProductByName(String name);

    Boolean existsByName(String name);

    @Transactional
    @Modifying
    void deleteProductEntityById(Long id);

    @Query(
            value = "SELECT product_id from order_product;",
            nativeQuery = true
    )
    List<Long> findAllProductsInOrders();

    @Query(
            value = "SELECT * FROM products p ORDER BY p.times_bought DESC LIMIT 5 ",
            nativeQuery = true
    )
    List<ProductEntity> findAllByTimesBought();


}
