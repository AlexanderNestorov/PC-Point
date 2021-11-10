package com.example.pcpoint.model.service.review;


public class ReviewUpdateServiceModel {

    private Long id;

    private String text;

    private Long product_id;

    private Long user_id;

    public Long getId() {
        return id;
    }

    public ReviewUpdateServiceModel setId(Long id) {
        this.id = id;
        return this;
    }

    public String getText() {
        return text;
    }

    public ReviewUpdateServiceModel setText(String text) {
        this.text = text;
        return this;
    }

    public Long getProduct_id() {
        return product_id;
    }

    public ReviewUpdateServiceModel setProduct_id(Long product_id) {
        this.product_id = product_id;
        return this;
    }

    public Long getUser_id() {
        return user_id;
    }

    public ReviewUpdateServiceModel setUser_id(Long user_id) {
        this.user_id = user_id;
        return this;
    }
}
