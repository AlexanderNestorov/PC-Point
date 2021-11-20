package com.example.pcpoint.repository.review;

import com.example.pcpoint.model.entity.review.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {

    Optional<ReviewEntity> findReviewById(Long id);

    @Query(
            value = "SELECT * from reviews r WHERE r.product_id = ?1",
            nativeQuery = true
    )
    List<ReviewEntity> findAllByProductId(Long productId);

    @Query(
            value = "SELECT * from reviews r WHERE r.reviewer_id = ?1",
            nativeQuery = true
    )
    List<ReviewEntity> findAllByReviewer(Long reviewerId);

    @Modifying
    @Transactional
    @Query(
            value = "DELETE from reviews r WHERE r.product_id = ?1",
            nativeQuery = true
    )
    void deleteByProduct(Long productId);
}
