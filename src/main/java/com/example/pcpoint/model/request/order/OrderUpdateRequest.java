package com.example.pcpoint.model.request.order;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

public class OrderUpdateRequest {

    @Positive
    private Long id;

    @Positive
    private Long buyer;

    @Valid
    private List<Long> products;

    public Long getId() {
        return id;
    }

    public OrderUpdateRequest setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getBuyer() {
        return buyer;
    }

    public OrderUpdateRequest setBuyer(Long buyer) {
        this.buyer = buyer;
        return this;
    }

    public List<Long> getProducts() {
        return products;
    }

    public OrderUpdateRequest setProducts(List<Long> products) {
        this.products = products;
        return this;
    }
}
