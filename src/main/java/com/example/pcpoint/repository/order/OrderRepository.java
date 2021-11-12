package com.example.pcpoint.repository.order;

import com.example.pcpoint.model.entity.order.OrderEntity;
import com.example.pcpoint.model.entity.review.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity,Long> {

    Optional<OrderEntity> findOrderById(Long id);
}
