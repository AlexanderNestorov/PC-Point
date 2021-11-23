package com.example.pcpoint.model.request.order;



import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

public class OrderAddRequest {

    @Positive
    @NotNull
    private Long buyer;

    @Valid
    private List<Long> products;

    public Long getBuyer() {
        return buyer;
    }

    public OrderAddRequest setBuyer(Long buyer) {
        this.buyer = buyer;
        return this;
    }

    public List<Long> getProducts() {
        return products;
    }

    public OrderAddRequest setProducts(List<Long> products) {
        this.products = products;
        return this;
    }
}
