package com.example.pcpoint.model.service.review;

public class ReviewAddServiceModel {

    private String text;

    private Long product_id;

    private Long user_id;

    public String getText() {
        return text;
    }

    public ReviewAddServiceModel setText(String text) {
        this.text = text;
        return this;
    }

    public Long getProduct_id() {
        return product_id;
    }

    public ReviewAddServiceModel setProduct_id(Long product_id) {
        this.product_id = product_id;
        return this;
    }

    public Long getUser_id() {
        return user_id;
    }

    public ReviewAddServiceModel setUser_id(Long user_id) {
        this.user_id = user_id;
        return this;
    }
}
