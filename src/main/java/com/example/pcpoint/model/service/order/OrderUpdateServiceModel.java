package com.example.pcpoint.model.service.order;

import java.util.List;

public class OrderUpdateServiceModel {

    private Long id;

    private Long buyer;

    private List<Long> products;

    public Long getId() {
        return id;
    }

    public OrderUpdateServiceModel setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getBuyer() {
        return buyer;
    }

    public OrderUpdateServiceModel setBuyer(Long buyer) {
        this.buyer = buyer;
        return this;
    }

    public List<Long> getProducts() {
        return products;
    }

    public OrderUpdateServiceModel setProducts(List<Long> products) {
        this.products = products;
        return this;
    }
}
