package com.example.pcpoint.service.review;

import com.example.pcpoint.model.entity.review.ReviewEntity;
import com.example.pcpoint.model.service.review.ReviewAddServiceModel;
import com.example.pcpoint.model.service.review.ReviewUpdateServiceModel;

import java.util.List;

public interface ReviewService {

    void addReview(ReviewAddServiceModel reviewAddServiceModel);

    List<ReviewEntity> findAllReviews();

    ReviewEntity findReviewById(Long id);

    ReviewEntity updateReview(ReviewUpdateServiceModel reviewUpdateServiceModel);

    void deleteReview(Long id);

    List<ReviewEntity> findAllReviewsByProductId(Long id);

    List<ReviewEntity> findAllReviewsByUserId(Long id);

    void deleteAllReviewsByProductId(Long id);
}
