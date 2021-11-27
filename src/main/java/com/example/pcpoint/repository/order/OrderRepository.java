package com.example.pcpoint.repository.order;

import com.example.pcpoint.model.entity.order.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity,Long> {

    Optional<OrderEntity> findOrderById(Long id);


    @Query(
            value = "SELECT * from orders o where o.buyer_id= ?1",
            nativeQuery = true
    )
    List<OrderEntity> findAllByBuyer_Id(long id);
}
