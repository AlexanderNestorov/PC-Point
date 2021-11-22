package com.example.pcpoint.model.request.review;



import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class ReviewAddRequest {

    @NotBlank
    private String text;

    @Positive
    @NotNull
    private Long product_id;

    @Positive
    @NotNull
    private Long user_id;

    public String getText() {
        return text;
    }

    public ReviewAddRequest setText(String text) {
        this.text = text;
        return this;
    }

    public Long getProduct_id() {
        return product_id;
    }

    public ReviewAddRequest setProduct_id(Long product_id) {
        this.product_id = product_id;
        return this;
    }

    public Long getUser_id() {
        return user_id;
    }

    public ReviewAddRequest setUser_id(Long user_id) {
        this.user_id = user_id;
        return this;
    }
}
