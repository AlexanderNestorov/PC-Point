package com.example.pcpoint.repository;

import com.example.pcpoint.exception.ItemNotFoundException;
import com.example.pcpoint.model.entity.location.LocationEntity;
import com.example.pcpoint.model.entity.review.ReviewEntity;
import com.example.pcpoint.repository.product.ProductRepository;
import com.example.pcpoint.repository.review.ReviewRepository;
import com.example.pcpoint.repository.user.UserRepository;
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
public class ReviewRepositoryTest {

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
    }
    @Test
    @DisplayName("Testing if get method returns correct amount of locations")
    public void testGetAllMethod() {
        assertThat(reviewRepository.findAll().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Testing if add method works correctly")
    public void testAdd() {
        assertThat(reviewRepository.findAll().size()).isEqualTo(2);
        assertThat(reviewRepository.findById(reviewEntity1.getId()).get().getText()).isEqualTo("test1");
    }

    @Test
    @DisplayName("Testing if find by id method works correctly")
    public void testFindById() {
        ReviewEntity test = reviewRepository.findById(reviewEntity1.getId()).orElseThrow(
                () -> new ItemNotFoundException("Review not found")
        );
        assertThat(test).isNotNull();
        assertThat(test.getId()).isEqualTo(reviewEntity1.getId());
    }

    @Test
    @DisplayName("Testing if find by text method works correctly")
    public void testFindByText() {
        ReviewEntity test = reviewRepository.findReviewEntityByText(reviewEntity2.getText()).orElseThrow(
                () -> new ItemNotFoundException("Product not found")
        );
        assertThat(test).isNotNull();
        assertThat(test.getText()).isEqualTo(reviewEntity2.getText());
        assertThat(test.getId()).isEqualTo(reviewEntity2.getId());
    }

    @Test
    @DisplayName("Testing if delete by id method works correctly")
    public void testDeleteById() {
        ReviewEntity test = reviewRepository.findById(reviewEntity1.getId()).orElse(null);
        assertThat(test).isNotNull();

        reviewRepository.deleteById(test.getId());

        assertThat(reviewRepository.count()).isEqualTo(1);
        assertThat(reviewRepository.findById(reviewEntity1.getId()).orElse(null)).isNull();
    }

    @Test
    @DisplayName("Testing if find all by product_id method works correctly")
    public void testFindAllByCity() {
        List<ReviewEntity> test1 = reviewRepository.findAllByProductId(1L);
        List<ReviewEntity> test2 = reviewRepository.findAllByProductId(2L);

        assertThat(test1).isNotNull();
        assertThat(test1.size()).isEqualTo(1);

        assertThat(test2).isNotNull();
        assertThat(test2.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Testing if find all by reviewer method works correctly")
    public void testAllCities() {
        List<ReviewEntity> test1 = reviewRepository.findAllByReviewer(1L);
        List<ReviewEntity> test2 = reviewRepository.findAllByReviewer(2L);

        assertThat(test1).isNotNull();
        assertThat(test1.size()).isEqualTo(1);

        assertThat(test2).isNotNull();
        assertThat(test2.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Testing if delete by product method works correctly")
    public void testDeleteByProduct() {
        ReviewEntity test = reviewRepository.findById(reviewEntity1.getId()).orElse(null);
        assertThat(test).isNotNull();

        reviewRepository.deleteByProduct(1L);

        assertThat(reviewRepository.count()).isEqualTo(1);
        assertThat(reviewRepository.findById(reviewEntity1.getId()).orElse(null)).isNull();

        test = reviewRepository.findById(reviewEntity2.getId()).orElse(null);
        assertThat(test).isNotNull();

        reviewRepository.deleteByProduct(2L);

        assertThat(reviewRepository.count()).isEqualTo(0);
        assertThat(reviewRepository.findById(reviewEntity1.getId()).orElse(null)).isNull();
    }

}
