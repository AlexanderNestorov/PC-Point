package com.example.pcpoint.model.service.product;

import java.math.BigDecimal;

public class ProductUpdateServiceModel {

    private Long id;

    private String name;

    private String description;

    private String imageUrl;

    private int quantity;

    private BigDecimal price;

    private String type;

    public Long getId() {
        return id;
    }

    public ProductUpdateServiceModel setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public ProductUpdateServiceModel setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ProductUpdateServiceModel setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public ProductUpdateServiceModel setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public int getQuantity() {
        return quantity;
    }

    public ProductUpdateServiceModel setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public ProductUpdateServiceModel setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public String getType() {
        return type;
    }

    public ProductUpdateServiceModel setType(String type) {
        this.type = type;
        return this;
    }
}
