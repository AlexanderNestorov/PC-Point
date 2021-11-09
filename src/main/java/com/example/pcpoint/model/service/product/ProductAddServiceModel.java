package com.example.pcpoint.model.service.product;

import java.math.BigDecimal;

public class ProductAddServiceModel {

    private String name;

    private String description;

    private String imageUrl;

    private int quantity;

    private BigDecimal price;

    private String type;

    public String getName() {
        return name;
    }

    public ProductAddServiceModel setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ProductAddServiceModel setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public ProductAddServiceModel setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public int getQuantity() {
        return quantity;
    }

    public ProductAddServiceModel setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public ProductAddServiceModel setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public String getType() {
        return type;
    }

    public ProductAddServiceModel setType(String type) {
        this.type = type;
        return this;
    }
}
