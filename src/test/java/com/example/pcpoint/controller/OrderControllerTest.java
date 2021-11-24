package com.example.pcpoint.controller;

import com.example.pcpoint.exception.ItemNotFoundException;
import com.example.pcpoint.model.entity.order.OrderEntity;
import com.example.pcpoint.model.entity.product.ProductEntity;
import com.example.pcpoint.model.entity.user.UserEntity;
import com.example.pcpoint.model.request.order.OrderAddRequest;
import com.example.pcpoint.model.request.order.OrderUpdateRequest;
import com.example.pcpoint.model.response.JwtResponse;
import com.example.pcpoint.model.service.user.UserLoginServiceModel;
import com.example.pcpoint.repository.order.OrderRepository;
import com.example.pcpoint.repository.product.ProductRepository;
import com.example.pcpoint.repository.user.UserRepository;
import com.example.pcpoint.service.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@AutoConfigureMockMvc
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserService userService;

    private Long ORDER_ID1, ORDER_ID2;


    private ProductEntity productEntity1, productEntity2;

    private UserEntity userEntity1, userEntity2;

    private String userToken, adminToken;

    @BeforeEach
    public void setUp(){

        orderRepository.deleteAll();

        UserLoginServiceModel userLoginServiceModel = new UserLoginServiceModel();
        userLoginServiceModel.setUsername("admin");
        userLoginServiceModel.setPassword("1234");

        JwtResponse jwtResponseAdmin = userService.loginUser(userLoginServiceModel);

        adminToken = jwtResponseAdmin.getAccessToken();

        userLoginServiceModel.setUsername("user");
        userLoginServiceModel.setPassword("1234");

        JwtResponse jwtResponseUser = userService.loginUser(userLoginServiceModel);

        userToken = jwtResponseUser.getAccessToken();

        productEntity1 = productRepository.findProductById(1L)
                .orElseThrow(() -> new ItemNotFoundException("Product not found"));

        productEntity2 = productRepository.findProductById(2L)
                .orElseThrow(() -> new ItemNotFoundException("Product not found"));

        userEntity1 = userRepository.findById(1L)
                .orElseThrow(() -> new ItemNotFoundException("User not found"));

        userEntity2 = userRepository.findById(2L)
                .orElseThrow(() -> new ItemNotFoundException("User not found"));


        OrderEntity order1 = new OrderEntity();

        order1.setBuyer(userEntity1)
                .setProducts(List.of(productEntity1, productEntity2))
                .setExpected(Instant.now().plus(3, ChronoUnit.DAYS))
                .setTotal(BigDecimal.valueOf(productEntity1.getPrice().doubleValue() + productEntity2.getPrice().doubleValue()));

        ORDER_ID1 = orderRepository.save(order1).getId();

        OrderEntity order2 = new OrderEntity();

        order2.setBuyer(userEntity2)
                .setProducts(List.of(productEntity1, productEntity2))
                .setExpected(Instant.now().plus(3, ChronoUnit.DAYS))
                .setTotal(BigDecimal.valueOf(productEntity1.getPrice().doubleValue() + productEntity2.getPrice().doubleValue()));

        ORDER_ID2 = orderRepository.save(order2).getId();
    }

    @AfterEach
    public void tearDown(){
        orderRepository.deleteAll();
    }

    @Test
    @DisplayName("Testing if testing components load successfully")
    public void contextLoads(){
        assertThat(orderRepository).isNotNull();
        assertThat(mockMvc).isNotNull();
        assertThat(userService).isNotNull();
        assertThat(userRepository).isNotNull();
        assertThat(productRepository).isNotNull();
    }

    // 'All' tests
    @Test
    @DisplayName("Test if 'all' method status is correct when no auth")
    public void testOrdersReturnsCorrectStatusCodeNoAuth() throws Exception {
        this.mockMvc.perform(get("/api/order/all"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Test if 'all' method status is ok")
    public void testOrdersReturnsCorrectStatusCode() throws Exception {
        this.mockMvc.perform(get("/api/order/all")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Testing if 'all' method returns correct content type")
    public void testOrdersReturnsCorrectContentType() throws Exception {
        this.mockMvc.perform(get("/api/order/all")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(content().contentType("application/json"));
    }

    @Test
    @DisplayName("Testing if 'all' method returns correct number of orders")
    public void testOrdersReturnsCorrectNumberOfEntries() throws Exception {
        this.mockMvc.perform(get("/api/order/all")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @DisplayName("Testing if 'all' method returns correct orders")
    public void testOrdersReturnsCorrectEntries() throws Exception {
        this.mockMvc.perform(get("/api/order/all")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(ORDER_ID1.intValue())))
                .andExpect(jsonPath("$[1].id", is(ORDER_ID2.intValue())));
    }

    // 'Delete' tests

    @Test
    @DisplayName("Testing if 'delete' method returns correct status on missing auth")
    public void testDeleteNotAuthorized() throws Exception {
        this.mockMvc.perform(delete("/api/order/delete/{id}", ORDER_ID1))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Testing if 'delete' works with user auth")
    public void testDeleteAuthorizedUser() throws Exception {
        this.mockMvc.perform(delete("/api/order/delete/{id}", ORDER_ID1)
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk());

        assertThat(orderRepository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("Testing if 'delete' works with admin auth")
    public void testDeleteAuthorizedAdmin() throws Exception {
        this.mockMvc.perform(delete("/api/order/delete/{id}", ORDER_ID1)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());

        assertThat(orderRepository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("Testing if 'delete' method returns correct status when entity does not exist")
    public void testDeleteAuthorizedWithUnexistingEntity() throws Exception {
        long count = orderRepository.count();
        this.mockMvc.perform(delete("/api/order/delete/{id}", count + 1)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Testing if 'delete' method returns correct status when entity_id does not exist")
    public void testDeleteAuthorizedWithUnexistingEntityId() throws Exception {
        this.mockMvc.perform(delete("/api/order/delete/{id}", -1)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound());
    }

    // 'Add' tests

    @Test
    @DisplayName("Testing if 'add' method returns correct status when missing auth")
    public void testAddOrderNotAuthorized() throws Exception {

        OrderAddRequest orderAddRequest = new OrderAddRequest();

        orderAddRequest.
                setBuyer(userEntity1.getId())
                .setProducts(List.of(1L, 2L));

        ObjectMapper mapper = new ObjectMapper();

        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/order/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(orderAddRequest)))
                .andExpect(status().isUnauthorized());

    }

    @Test
    @DisplayName("Testing if 'add' method works")
    public void testAddOrderAuthorized() throws Exception {

        OrderAddRequest orderAddRequest = new OrderAddRequest();

        long count = orderRepository.count();

        orderAddRequest.
                setBuyer(userEntity1.getId())
                .setProducts(List.of(1L, 2L));

        ObjectMapper objectMapper = new ObjectMapper();

        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/order/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderAddRequest))
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());


        assertThat(orderRepository.count()).isEqualTo(count + 1);

    }

    @Test
    @DisplayName("Testing if 'add' method returns correct status when sent a bad request")
    public void testAddOrderAuthorizedBadRequest() throws Exception {

        OrderAddRequest orderAddRequest = new OrderAddRequest();

        orderAddRequest
//                setBuyer(userEntity1.getId())
                .setProducts(List.of(1L, 2L));

        ObjectMapper objectMapper = new ObjectMapper();

        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/order/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderAddRequest))
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isBadRequest());

    }

    // 'Update' tests
    @Test
    @DisplayName("Testing if 'update' method returns correct status when missing auth")
    public void testUpdateOrderNotAuthorized() throws Exception {

        OrderUpdateRequest orderUpdateRequest = new OrderUpdateRequest();

        orderUpdateRequest
                .setId(ORDER_ID1)
                .setBuyer(userEntity2.getId())
                .setProducts(List.of(1L, 2L));

        ObjectMapper mapper = new ObjectMapper();

        this.mockMvc.perform(MockMvcRequestBuilders.put("/api/order/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(orderUpdateRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Testing if 'update' method works")
    public void testUpdateOrderAuthorized() throws Exception {

        long count = orderRepository.count();

        OrderUpdateRequest orderUpdateRequest = new OrderUpdateRequest();

        orderUpdateRequest
                .setId(ORDER_ID1)
                .setBuyer(userEntity2.getId())
                .setProducts(List.of(1L, 2L));

        ObjectMapper objectMapper = new ObjectMapper();

        this.mockMvc.perform(MockMvcRequestBuilders.put("/api/order/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderUpdateRequest))
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk());

        assertThat(orderRepository.count()).isEqualTo(count);

    }

    @Test
    @DisplayName("Testing if 'update' method returns correct status when sent a bad request")
    public void testUpdateOrderAuthorizedBadRequest() throws Exception {

        OrderUpdateRequest orderUpdateRequest = new OrderUpdateRequest();

        orderUpdateRequest
                .setId(ORDER_ID1)
                //.setBuyer(userEntity2.getId())
                .setProducts(List.of(1L, 2L));

        ObjectMapper objectMapper = new ObjectMapper();

        this.mockMvc.perform(MockMvcRequestBuilders.put("/api/order/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderUpdateRequest))
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Testing if 'update' method returns correct status when required entity is not found")
    public void testUpdateOrderAuthorizedNotFound() throws Exception {

        long count = orderRepository.count();

        OrderUpdateRequest orderUpdateRequest = new OrderUpdateRequest();

        orderUpdateRequest
                .setId(count + 1)
                .setBuyer(userEntity2.getId())
                .setProducts(List.of(1L, 2L));

        ObjectMapper objectMapper = new ObjectMapper();

        this.mockMvc.perform(MockMvcRequestBuilders.put("/api/order/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderUpdateRequest))
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound());

    }

    // 'by user' tests
    @Test
    @DisplayName("Testing if 'by user' method returns correct status when missing auth")
    public void testGetOrdersByUserNotAuthorized() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/order/by_user/" + userEntity1.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Testing if 'by user' method returns correct data length when authorized")
    public void testGetOrdersByUserAuthorizedLength() throws Exception {

        this.mockMvc.perform(get("/api/order/by_user/1")
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @DisplayName("Testing if 'by user' method returns correct data when authorized")
    public void testGetOrdersByUserAuthorized() throws Exception {

        this.mockMvc.perform(get("/api/order/by_user/1")
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(ORDER_ID1.intValue())));
    }

    @Test
    @DisplayName("Testing if 'by user' method returns correct status when invalid user id")
    public void testGetOrdersByUserInvalid() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/order/by_user/" + "-1")
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Testing if 'by user' method returns correct status when not existing user id")
    public void testGetOrdersByUserNotFound() throws Exception {

        long count = userRepository.count();

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/order/by_user/" + (count + 1))
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isNotFound());
    }
}
