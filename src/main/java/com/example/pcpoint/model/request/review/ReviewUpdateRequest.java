package com.example.pcpoint.model.request.review;

import javax.validation.constraints.NotBlank;

public class ReviewUpdateRequest {

    @NotBlank
    private Long id;

    @NotBlank
    private String text;

    @NotBlank
    private Long product_id;

    @NotBlank
    private Long user_id;

    public Long getId() {
        return id;
    }

    public ReviewUpdateRequest setId(Long id) {
        this.id = id;
        return this;
    }

    public String getText() {
        return text;
    }

    public ReviewUpdateRequest setText(String text) {
        this.text = text;
        return this;
    }

    public Long getProduct_id() {
        return product_id;
    }

    public ReviewUpdateRequest setProduct_id(Long product_id) {
        this.product_id = product_id;
        return this;
    }

    public Long getUser_id() {
        return user_id;
    }

    public ReviewUpdateRequest setUser_id(Long user_id) {
        this.user_id = user_id;
        return this;
    }
}
