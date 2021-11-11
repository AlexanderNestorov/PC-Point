package com.example.pcpoint.model.request.product;


import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

public class ProductAddRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotBlank
    private String imageUrl;

    @Min(0)
    private int quantity;

    @Min(0)
    private BigDecimal price;

    @NotBlank
    private String type;

    public String getName() {
        return name;
    }

    public ProductAddRequest setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ProductAddRequest setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public ProductAddRequest setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public int getQuantity() {
        return quantity;
    }

    public ProductAddRequest setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public ProductAddRequest setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public String getType() {
        return type;
    }

    public ProductAddRequest setType(String type) {
        this.type = type;
        return this;
    }
}
