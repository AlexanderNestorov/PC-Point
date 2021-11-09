package com.example.pcpoint.model.request.product;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

public class ProductUpdateRequest {

    @Positive
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotBlank
    private String imageUrl;

    @NotBlank
    @PositiveOrZero
    private int quantity;

    @NotBlank
    @Positive
    private BigDecimal price;

    @NotBlank
    private String type;

    public Long getId() {
        return id;
    }

    public ProductUpdateRequest setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public ProductUpdateRequest setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ProductUpdateRequest setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public ProductUpdateRequest setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public int getQuantity() {
        return quantity;
    }

    public ProductUpdateRequest setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public ProductUpdateRequest setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public String getType() {
        return type;
    }

    public ProductUpdateRequest setType(String type) {
        this.type = type;
        return this;
    }
}
