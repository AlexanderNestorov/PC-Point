package com.example.pcpoint.controller;

import com.example.pcpoint.model.entity.review.ReviewEntity;
import com.example.pcpoint.model.request.review.ReviewAddRequest;
import com.example.pcpoint.model.request.review.ReviewUpdateRequest;
import com.example.pcpoint.model.response.MessageResponse;
import com.example.pcpoint.model.service.review.ReviewAddServiceModel;
import com.example.pcpoint.model.service.review.ReviewUpdateServiceModel;
import com.example.pcpoint.service.product.ProductService;
import com.example.pcpoint.service.review.ReviewService;
import com.example.pcpoint.service.user.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/review")
public class ReviewController {

    private final ReviewService reviewService;

    private final ModelMapper modelMapper;

    private final ProductService productService;

    private final UserService userService;

    public ReviewController(ReviewService reviewService, ModelMapper modelMapper, ProductService productService, UserService userService) {
        this.reviewService = reviewService;
        this.modelMapper = modelMapper;
        this.productService = productService;
        this.userService = userService;
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllReviews() {
        List<ReviewEntity> reviews = this.reviewService.findAllReviews();
        return ResponseEntity.ok(reviews);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addReview(@Valid @RequestBody ReviewAddRequest reviewAddRequest,
                                       BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid review request data!"));
        }

        ReviewAddServiceModel reviewAddServiceModel =
                modelMapper.map(reviewAddRequest, ReviewAddServiceModel.class);


        reviewService.addReview(reviewAddServiceModel);


        return ResponseEntity.ok(new MessageResponse("Review added successfully!"));
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateReview(@Valid @RequestBody ReviewUpdateRequest reviewUpdateRequest,
                                          BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid review request data!"));
        }

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

    @GetMapping("/by_product/{id}")
    public ResponseEntity<?> getReviewsByProductId(@PathVariable("id") Long id) {

        Long product = this.productService.findProductById(id).getId();

        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        List<ReviewEntity> reviews = this.reviewService.findAllReviewsByProductId(id);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/by_user/{id}")
    public ResponseEntity<?> getReviewsByUserId(@PathVariable("id") Long id) {

        Long user = this.userService.findById(id).getId();

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        List<ReviewEntity> reviews = this.reviewService.findAllReviewsByUserId(id);
        return ResponseEntity.ok(reviews);
    }
}
