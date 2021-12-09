package com.example.pcpoint.service;

import com.example.pcpoint.exception.ItemNotFoundException;
import com.example.pcpoint.model.entity.review.ReviewEntity;
import com.example.pcpoint.model.service.review.ReviewAddServiceModel;
import com.example.pcpoint.model.service.review.ReviewUpdateServiceModel;
import com.example.pcpoint.repository.product.ProductRepository;
import com.example.pcpoint.repository.review.ReviewRepository;
import com.example.pcpoint.repository.user.UserRepository;
import com.example.pcpoint.service.review.ReviewService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ReviewServiceTest {


    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    ReviewEntity reviewEntity1, reviewEntity2;


    @BeforeEach
    public void setUp() {
        reviewRepository.deleteAll();


        ReviewEntity testEntity1 = new ReviewEntity();
        testEntity1.setText("test1")
                .setReviewer(userRepository.findById(1L).orElseThrow(
                        () -> new ItemNotFoundException("User not found")
                ))
                .setProduct(productRepository.findById(1L).orElseThrow(
                        () -> new ItemNotFoundException("Product not found")
                ));

        ReviewEntity testEntity2 = new ReviewEntity();
        testEntity2.setText("test2")
                .setReviewer(userRepository.findById(2L).orElseThrow(
                        () -> new ItemNotFoundException("User not found")
                ))
                .setProduct(productRepository.findById(2L).orElseThrow(
                        () -> new ItemNotFoundException("Product not found")
                ));

        reviewEntity1 = reviewRepository.save(testEntity1);
        reviewEntity2 = reviewRepository.save(testEntity2);

    }

    @AfterEach
    public void tearDown() {
        reviewRepository.deleteAll();
    }

    @Test
    @DisplayName("Testing if testing components load successfully")
    public void contextLoads() {
        assertThat(reviewRepository).isNotNull();
        assertThat(userRepository).isNotNull();
        assertThat(productRepository).isNotNull();
        assertThat(reviewService).isNotNull();
    }

    @Test
    @DisplayName("Testing if get method returns correct amount of reviews")
    public void testGetAllMethod() {
        assertThat(reviewService.findAllReviews().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Testing if add method works correctly")
    public void testAdd() {
        ReviewAddServiceModel reviewAddServiceModel = new ReviewAddServiceModel();

        assertThat(reviewService.findAllReviews().size()).isEqualTo(2);

        reviewAddServiceModel
                .setText("test")
                .setProduct_id(3L)
                .setUser_id(2L);


        reviewService.addReview(reviewAddServiceModel);



        assertThat(reviewService.findAllReviews().size()).isEqualTo(3);
    }

    @Test
    @DisplayName("Testing if update method works correctly")
    public void testUpdate() {
        ReviewUpdateServiceModel reviewUpdateServiceModel = new ReviewUpdateServiceModel();

        reviewUpdateServiceModel
                .setId(reviewEntity1.getId())
                .setText("Update")
                .setProduct_id(1L)
                .setUser_id(1L);

        reviewService.updateReview(reviewUpdateServiceModel);

        assertThat(reviewService.findAllReviews().size()).isEqualTo(2);
        assertThat(reviewService.findReviewById(reviewEntity1.getId()).getText()).isEqualTo("Update");
    }

    @Test
    @DisplayName("Testing if find by id method works correctly")
    public void testFindById() {
        ReviewEntity test = reviewService.findReviewById(reviewEntity1.getId());
        assertThat(test).isNotNull();
        assertThat(test.getId()).isEqualTo(reviewEntity1.getId());
    }

    @Test
    @DisplayName("Testing if delete by id method works correctly")
    public void testDeleteById() {
        reviewService.deleteReview(reviewEntity1.getId());

        assertThat(reviewService.findAllReviews().size()).isEqualTo(1);
    }


    @Test
    @DisplayName("Testing if find all by product method works correctly")
    public void testFindAllByProduct() {
        List<ReviewEntity> list = reviewService.findAllReviewsByProductId(1L);
        assertThat(list.size()).isEqualTo(1);

        assertThat(list.get(0).getText()).isEqualTo("test1");
    }

    @Test
    @DisplayName("Testing if find all by user method works correctly")
    public void testFindAllByUser() {
        List<ReviewEntity> list = reviewService.findAllReviewsByUserId(2L);
        assertThat(list.size()).isEqualTo(1);

        assertThat(list.get(0).getText()).isEqualTo("test2");
    }

    @Test
    @DisplayName("Testing if delete by product method works correctly")
    public void testDeleteByProduct() {
        reviewService.deleteAllReviewsByProductId(reviewEntity1.getProduct().getId());

        assertThat(reviewService.findAllReviews().size()).isEqualTo(1);
    }
}
