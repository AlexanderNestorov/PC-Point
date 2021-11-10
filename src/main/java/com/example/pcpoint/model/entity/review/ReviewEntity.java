package com.example.pcpoint.model.entity.review;

import com.example.pcpoint.model.entity.BaseEntity;
import com.example.pcpoint.model.entity.product.ProductEntity;
import com.example.pcpoint.model.entity.user.UserEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "reviews")
public class ReviewEntity extends BaseEntity {

    @Column(nullable = false, columnDefinition = "TEXT")
    private String text;

    @ManyToOne
    private ProductEntity product;

    @ManyToOne
    private UserEntity seller;

    public String getText() {
        return text;
    }

    public ReviewEntity setText(String text) {
        this.text = text;
        return this;
    }

    public ProductEntity getProduct() {
        return product;
    }

    public ReviewEntity setProduct(ProductEntity product) {
        this.product = product;
        return this;
    }

    public UserEntity getSeller() {
        return seller;
    }

    public ReviewEntity setSeller(UserEntity seller) {
        this.seller = seller;
        return this;
    }
}
