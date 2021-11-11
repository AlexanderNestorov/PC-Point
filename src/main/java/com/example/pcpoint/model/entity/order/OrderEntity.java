package com.example.pcpoint.model.entity.order;

import com.example.pcpoint.model.entity.BaseEntity;
import com.example.pcpoint.model.entity.product.ProductEntity;
import com.example.pcpoint.model.entity.user.UserEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "orders")
public class OrderEntity extends BaseEntity {

    @OneToMany
    private List<ProductEntity> products;

    @ManyToOne
    private UserEntity seller;

    @Column(nullable = false)
    private Instant expected;

    @Column(nullable = false)
    private BigDecimal total;




}
