package com.example.pcpoint.model.entity.product;



import com.example.pcpoint.model.enums.ProductTypeEnum;

import javax.persistence.*;

@Entity
@Table(name = "types")
public class ProductTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductTypeEnum type;

    public Long getId() {
        return id;
    }

    public ProductTypeEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public ProductTypeEnum getType() {
        return type;
    }

    public ProductTypeEntity setType(ProductTypeEnum type) {
        this.type = type;
        return this;
    }
}
