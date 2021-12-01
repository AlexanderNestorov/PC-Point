package com.example.pcpoint.repository;

import com.example.pcpoint.exception.ItemNotFoundException;
import com.example.pcpoint.model.entity.order.OrderEntity;
import com.example.pcpoint.repository.order.OrderRepository;
import com.example.pcpoint.repository.product.ProductRepository;
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

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    OrderEntity orderEntity1, orderEntity2;

    @BeforeEach
    public void setUp() {
        orderRepository.deleteAll();



        OrderEntity testEntity1 = new OrderEntity();
        testEntity1.setProducts(List.of(productRepository.findProductById(1L).orElseThrow(
                () -> new ItemNotFoundException("Product not found")
        ), productRepository.findProductById(2L).orElseThrow(
                () -> new ItemNotFoundException("Product not found"))))
                .setBuyer(userRepository.findById(1L).orElseThrow(
                        () -> new ItemNotFoundException("User not found")))
                .setTotal(BigDecimal.valueOf(productRepository.findProductById(1L).orElseThrow(
                        () -> new ItemNotFoundException("Product not found")).getPrice().doubleValue()
                         + productRepository.findProductById(2L).orElseThrow(
                        () -> new ItemNotFoundException("Product not found")).getPrice().doubleValue()))
                .setExpected(Instant.now().plus(3, java.time.temporal.ChronoUnit.DAYS));

        OrderEntity testEntity2 = new OrderEntity();
        testEntity2.setProducts(List.of(productRepository.findProductById(3L).orElseThrow(
                        () -> new ItemNotFoundException("Product not found")
                ), productRepository.findProductById(4L).orElseThrow(
                        () -> new ItemNotFoundException("Product not found"))))
                .setBuyer(userRepository.findById(2L).orElseThrow(
                        () -> new ItemNotFoundException("User not found")))
                .setTotal(BigDecimal.valueOf(productRepository.findProductById(3L).orElseThrow(
                        () -> new ItemNotFoundException("Product not found")).getPrice().doubleValue()
                        + productRepository.findProductById(4L).orElseThrow(
                        () -> new ItemNotFoundException("Product not found")).getPrice().doubleValue()))
                .setExpected(Instant.now().plus(3, java.time.temporal.ChronoUnit.DAYS));

        orderEntity1 = orderRepository.save(testEntity1);
        orderEntity2 = orderRepository.save(testEntity2);

    }

    @AfterEach
    public void tearDown() {
        orderRepository.deleteAll();
    }


    @Test
    @DisplayName("Testing if testing components load successfully")
    public void contextLoads() {
        assertThat(orderRepository).isNotNull();
        assertThat(productRepository).isNotNull();
        assertThat(userRepository).isNotNull();
    }

    @Test
    @DisplayName("Testing if get method returns correct amount of orders")
    public void testGetAllMethod() {
        assertThat(orderRepository.findAll().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Testing if add method works correctly")
    public void testAdd() {
        assertThat(orderRepository.findAll().size()).isEqualTo(2);
        assertThat(orderRepository.findById(orderEntity1.getId()).get().getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Testing if find by id method works correctly")
    public void testFindById() {
        OrderEntity test = orderRepository.findById(orderEntity1.getId()).orElseThrow(
                () -> new ItemNotFoundException("Order not found")
        );
        assertThat(test).isNotNull();
        assertThat(test.getId()).isEqualTo(orderEntity1.getId());
    }

    @Test
    @DisplayName("Testing if delete by id method works correctly")
    public void testDeleteById() {
        OrderEntity test = orderRepository.findById(orderEntity1.getId()).orElse(null);
        assertThat(test).isNotNull();

        orderRepository.deleteById(test.getId());

        assertThat(orderRepository.count()).isEqualTo(1);
        assertThat(orderRepository.findById(orderEntity1.getId()).orElse(null)).isNull();
    }

    @Test
    @DisplayName("Testing if find all by buyer id method works correctly")
    public void testFindAllByBuyer() {
        List<OrderEntity> test = orderRepository.findAllByBuyer_Id(1L);
        assertThat(test).isNotNull();
        assertThat(test.size()).isEqualTo(1);

        test = orderRepository.findAllByBuyer_Id(2L);
        assertThat(test).isNotNull();
        assertThat(test.size()).isEqualTo(1);
    }


}
