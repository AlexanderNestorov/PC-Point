package com.example.pcpoint.model.entity.product;

import com.example.pcpoint.model.entity.BaseEntity;

import javax.persistence.*;


@Entity
@Table(name = "products")
public class ProductEntity extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private int quantity;

    @ManyToOne
    private ProductTypeEntity type;

    public String getName() {
        return name;
    }

    public ProductEntity setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ProductEntity setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public ProductEntity setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public int getQuantity() {
        return quantity;
    }

    public ProductEntity setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public ProductTypeEntity getType() {
        return type;
    }

    public ProductEntity setType(ProductTypeEntity type) {
        this.type = type;
        return this;
    }
}
