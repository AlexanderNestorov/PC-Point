package com.example.pcpoint.service.review;

import com.example.pcpoint.exception.ItemNotFoundException;
import com.example.pcpoint.model.entity.review.ReviewEntity;
import com.example.pcpoint.model.service.review.ReviewAddServiceModel;
import com.example.pcpoint.model.service.review.ReviewUpdateServiceModel;
import com.example.pcpoint.repository.product.ProductRepository;
import com.example.pcpoint.repository.review.ReviewRepository;
import com.example.pcpoint.repository.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void addReview(ReviewAddServiceModel reviewAddServiceModel) {
        ReviewEntity reviewEntity = new ReviewEntity();


        reviewEntity
                .setText(reviewAddServiceModel.getText())
                .setProduct(productRepository.findById(reviewAddServiceModel.getProduct_id()).orElse(null))
                .setSeller(userRepository.findById(reviewAddServiceModel.getUser_id()).orElse(null));



        reviewRepository.save(reviewEntity);
    }

    @Override
    public List<ReviewEntity> findAllReviews() {
        return reviewRepository.findAll();
    }

    @Override
    public ReviewEntity findReviewById(Long id) {
        return reviewRepository.findReviewById(id)
                .orElseThrow(() -> new ItemNotFoundException("Review Entity with id " + id + " was not found"));
    }

    @Override
    public ReviewEntity updateReview(ReviewUpdateServiceModel reviewUpdateServiceModel) {
        ReviewEntity review = reviewRepository.findById(reviewUpdateServiceModel.getId())
                .orElseThrow(() -> new ItemNotFoundException("Review Entity with id " + reviewUpdateServiceModel.getId() + " was not found"));
        if (review == null) {
            return null;
        }




        review
                .setText(reviewUpdateServiceModel.getText())
                .setProduct(productRepository.findById(reviewUpdateServiceModel.getProduct_id()).orElse(null))
                .setSeller(userRepository.findById(reviewUpdateServiceModel.getUser_id()).orElse(null));


        return reviewRepository.save(review);
    }

    @Override
    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }
}
