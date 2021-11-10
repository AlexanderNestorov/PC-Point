package com.example.pcpoint.controller;

import com.example.pcpoint.model.entity.review.ReviewEntity;
import com.example.pcpoint.model.request.review.ReviewAddRequest;
import com.example.pcpoint.model.request.review.ReviewUpdateRequest;
import com.example.pcpoint.model.response.MessageResponse;
import com.example.pcpoint.model.service.review.ReviewAddServiceModel;
import com.example.pcpoint.model.service.review.ReviewUpdateServiceModel;
import com.example.pcpoint.service.review.ReviewService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/review")
public class ReviewController {

    private final ReviewService reviewService;

    private final ModelMapper modelMapper;

    public ReviewController(ReviewService reviewService, ModelMapper modelMapper) {
        this.reviewService = reviewService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllReviews() {
        List<ReviewEntity> reviews = this.reviewService.findAllReviews();
        return ResponseEntity.ok(reviews);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addReview(@Valid @RequestBody ReviewAddRequest reviewAddRequest) {

        ReviewAddServiceModel reviewAddServiceModel =
                modelMapper.map(reviewAddRequest, ReviewAddServiceModel.class);


        reviewService.addReview(reviewAddServiceModel);


        return ResponseEntity.ok(new MessageResponse("Review added successfully!"));
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateReview(@Valid @RequestBody ReviewUpdateRequest reviewUpdateRequest) {
        ReviewUpdateServiceModel reviewUpdateServiceModel =
                modelMapper.map(reviewUpdateRequest, ReviewUpdateServiceModel.class);


        ReviewEntity updated = reviewService.updateReview(reviewUpdateServiceModel);


        return ResponseEntity.ok(updated);

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") Long id) {

        Long deletionId = reviewService.findReviewById(id).getId();

        if(deletionId == null){
            return ResponseEntity.notFound().build();
        }

        this.reviewService.deleteReview(id);

        return ResponseEntity.ok(new MessageResponse("Review deleted successfully!"));
    }
}
