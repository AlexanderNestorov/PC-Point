package com.example.pcpoint.service;

import com.example.pcpoint.exception.ItemNotFoundException;
import com.example.pcpoint.model.entity.order.OrderEntity;
import com.example.pcpoint.model.service.order.OrderAddServiceModel;
import com.example.pcpoint.model.service.order.OrderUpdateServiceModel;
import com.example.pcpoint.repository.order.OrderRepository;
import com.example.pcpoint.repository.product.ProductRepository;
import com.example.pcpoint.repository.user.UserRepository;
import com.example.pcpoint.service.order.OrderService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)

public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

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
        testEntity1
                .setExpected(Instant.now().plus(3, java.time.temporal.ChronoUnit.DAYS))
                .setProducts(List.of(productRepository.findById(1L).orElseThrow(ItemNotFoundException::new),
                        productRepository.findById(2L).orElseThrow(ItemNotFoundException::new)))
                .setTotal(productRepository.findById(1L).orElseThrow(ItemNotFoundException::new).getPrice()
                                .add(productRepository.findById(2L).orElseThrow(ItemNotFoundException::new).getPrice()))
                .setBuyer(userRepository.findById(1L).orElseThrow(ItemNotFoundException::new));

        OrderEntity testEntity2= new OrderEntity();
        testEntity2
                .setExpected(Instant.now().plus(3, java.time.temporal.ChronoUnit.DAYS))
                .setProducts(List.of(productRepository.findById(3L).orElseThrow(ItemNotFoundException::new),
                        productRepository.findById(4L).orElseThrow(ItemNotFoundException::new)))
                .setTotal(productRepository.findById(3L).orElseThrow(ItemNotFoundException::new).getPrice()
                        .add(productRepository.findById(4L).orElseThrow(ItemNotFoundException::new).getPrice()))
                .setBuyer(userRepository.findById(2L).orElseThrow(ItemNotFoundException::new));



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
        assertThat(orderService).isNotNull();
        assertThat(userRepository).isNotNull();
        assertThat(productRepository).isNotNull();
        assertThat(orderRepository).isNotNull();
    }

    @Test
    @DisplayName("Testing if get method returns correct amount of orders")
    public void testGetAllMethod() {
        assertThat(orderService.findAllOrders().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Testing if add method works correctly")
    public void testAdd() {
        OrderAddServiceModel orderAddServiceModel = new OrderAddServiceModel();

        assertThat(orderService.findAllOrders().size()).isEqualTo(2);

        orderAddServiceModel
                .setBuyer(1L)
                .setProducts(List.of(1L,2L,3L));


        orderService.addOrder(orderAddServiceModel);



        assertThat(orderService.findAllOrders().size()).isEqualTo(3);
    }

    @Test
    @DisplayName("Testing if update method works correctly")
    public void testUpdate() {
        OrderUpdateServiceModel orderUpdateServiceModel = new OrderUpdateServiceModel();

        orderUpdateServiceModel
                .setId(orderEntity1.getId())
                .setBuyer(2L)
                .setProducts(List.of(1L,2L,3L,4L));

        orderService.updateOrder(orderUpdateServiceModel);

        assertThat(orderService.findAllOrders().size()).isEqualTo(2);
        assertThat(orderService.findOrderById(orderEntity1.getId()).getBuyer().getId()).isEqualTo(2L);
    }

    @Test
    @DisplayName("Testing if find by id method works correctly")
    public void testFindById() {
        OrderEntity test = orderService.findOrderById(orderEntity1.getId());
        assertThat(test).isNotNull();
        assertThat(test.getId()).isEqualTo(orderEntity1.getId());
    }

    @Test
    @DisplayName("Testing if delete by id method works correctly")
    public void testDeleteById() {
        orderService.deleteOrder(orderEntity1.getId());

        assertThat(orderService.findAllOrders().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Testing if find all by user method works correctly")
    public void testFindAllByUser() {
        List<OrderEntity> list = orderService.findAllOrdersByBuyer(1L);
        assertThat(list.size()).isEqualTo(1);

        assertThat(list.get(0).getBuyer().getId()).isEqualTo(1L);
    }
}
