package com.example.pcpoint.model.service.order;

import java.util.List;

public class OrderAddServiceModel {

    private Long buyer;

    private List<Long> products;

    public Long getBuyer() {
        return buyer;
    }

    public OrderAddServiceModel setBuyer(Long buyer) {
        this.buyer = buyer;
        return this;
    }

    public List<Long> getProducts() {
        return products;
    }

    public OrderAddServiceModel setProducts(List<Long> products) {
        this.products = products;
        return this;
    }
}
