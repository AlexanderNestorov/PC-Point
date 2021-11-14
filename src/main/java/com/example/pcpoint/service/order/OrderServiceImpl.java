package com.example.pcpoint.service.order;

import com.example.pcpoint.exception.ItemNotFoundException;
import com.example.pcpoint.model.entity.order.OrderEntity;
import com.example.pcpoint.model.entity.product.ProductEntity;
import com.example.pcpoint.model.entity.review.ReviewEntity;
import com.example.pcpoint.model.service.order.OrderAddServiceModel;
import com.example.pcpoint.model.service.order.OrderUpdateServiceModel;
import com.example.pcpoint.repository.order.OrderRepository;
import com.example.pcpoint.repository.product.ProductRepository;
import com.example.pcpoint.repository.user.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public OrderServiceImpl(OrderRepository orderRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void addOrder(OrderAddServiceModel orderAddServiceModel) {
        OrderEntity orderEntity = new OrderEntity();

        List<ProductEntity> products = defineProducts(orderAddServiceModel.getProducts());

        BigDecimal total = calculateTotal(products);

        orderEntity
                .setBuyer(userRepository.findById(orderAddServiceModel.getBuyer())
                        .orElseThrow(() -> new ItemNotFoundException("Buyer not found")))
                .setProducts(products)
                .setExpected(Instant.now().plus(3, ChronoUnit.DAYS))
                .setTotal(total);




        orderRepository.save(orderEntity);
    }

    @Override
    public List<OrderEntity> findAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public OrderEntity findOrderById(Long id) {
        return orderRepository.findOrderById(id)
                .orElseThrow(() -> new ItemNotFoundException("Order Entity with id " + id + " was not found"));
    }

    @Override
    public OrderEntity updateOrder(OrderUpdateServiceModel orderUpdateServiceModel) {
        OrderEntity order = orderRepository.findById(orderUpdateServiceModel.getId())
                .orElseThrow(() -> new ItemNotFoundException("Order Entity with id " + orderUpdateServiceModel.getId() + " was not found"));
        if (order == null) {
            return null;
        }


        List<ProductEntity> products = defineProducts(orderUpdateServiceModel.getProducts());

        BigDecimal total = calculateTotal(products);


        order
                .setBuyer(userRepository.findById(orderUpdateServiceModel.getBuyer())
                        .orElseThrow(() -> new ItemNotFoundException("Buyer not found")))
                .setProducts(products)
                .setExpected(Instant.now().plus(3, ChronoUnit.DAYS))
                .setTotal(total);


        return orderRepository.save(order);
    }

    @Override
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    private List<ProductEntity> defineProducts(List<Long> products) {
        List<ProductEntity> productEntities = new ArrayList<>();
        for (Long productId : products) {
            ProductEntity productEntity = this.productRepository.findById(productId)
                    .orElseThrow(() -> new ItemNotFoundException("Product not found!"));
            productEntity.setQuantity(productEntity.getQuantity() - 1);
            productRepository.save(productEntity);
            productEntities.add(productEntity);
        }
        return productEntities;
    }

    private BigDecimal calculateTotal(List<ProductEntity> products) {
        BigDecimal total = new BigDecimal(0);
        for (ProductEntity product : products) {
            total = total.add(product.getPrice());
        }
        return total;
    }

}
