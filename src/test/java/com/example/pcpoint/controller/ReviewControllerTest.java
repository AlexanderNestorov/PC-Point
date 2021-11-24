package com.example.pcpoint.controller;

import com.example.pcpoint.exception.ItemNotFoundException;
import com.example.pcpoint.model.entity.product.ProductEntity;
import com.example.pcpoint.model.entity.review.ReviewEntity;
import com.example.pcpoint.model.entity.user.UserEntity;
import com.example.pcpoint.model.request.review.ReviewAddRequest;
import com.example.pcpoint.model.request.review.ReviewUpdateRequest;
import com.example.pcpoint.model.response.JwtResponse;
import com.example.pcpoint.model.service.user.UserLoginServiceModel;
import com.example.pcpoint.repository.product.ProductRepository;
import com.example.pcpoint.repository.review.ReviewRepository;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@AutoConfigureMockMvc
public class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserService userService;

    private Long REVIEW_ID1, REVIEW_ID2;

    private String TEXT_1 = "review text 1", TEXT_2 = "review text 2";

    private ProductEntity productEntity1;
    private ProductEntity productEntity2;

    private UserEntity userEntity1;
    private UserEntity userEntity2;

    private String userToken;
    private String adminToken;

    @BeforeEach
    public void setUp(){

        reviewRepository.deleteAll();

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


        ReviewEntity review1 = new ReviewEntity();

        review1.setText(TEXT_1)
                .setReviewer(userEntity1)
                .setProduct(productEntity1);

        REVIEW_ID1 = reviewRepository.save(review1).getId();

        ReviewEntity review2 = new ReviewEntity();

        review2.setText(TEXT_2)
                .setReviewer(userEntity2)
                .setProduct(productEntity2);

        REVIEW_ID2 = reviewRepository.save(review2).getId();
    }

    @AfterEach
    public void tearDown(){
        reviewRepository.deleteAll();
    }

    @Test
    @DisplayName("Testing if testing components load successfully")
    public void contextLoads(){
        assertThat(reviewRepository).isNotNull();
        assertThat(mockMvc).isNotNull();
        assertThat(userService).isNotNull();
        assertThat(userRepository).isNotNull();
        assertThat(productRepository).isNotNull();
    }

    // 'All' tests
    @Test
    @DisplayName("Test if 'all' method status is ok")
    public void testReviewsReturnsCorrectStatusCode() throws Exception {
        this.mockMvc.perform(get("/api/review/all"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Testing if 'all' method returns correct content type")
    public void testReviewsReturnsCorrectContentType() throws Exception {
        this.mockMvc.perform(get("/api/review/all"))
                .andExpect(content().contentType("application/json"));
    }

    @Test
    @DisplayName("Testing if 'all' method returns correct number of reviews")
    public void testReviewsReturnsCorrectNumberOfEntries() throws Exception {
        this.mockMvc.perform(get("/api/review/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @DisplayName("Testing if 'all' method returns correct reviews")
    public void testReviewsReturnsCorrectEntries() throws Exception {
        this.mockMvc.perform(get("/api/review/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(REVIEW_ID1.intValue())))
                .andExpect(jsonPath("$[1].id", is(REVIEW_ID2.intValue())));
    }

    // 'Delete' tests

    @Test
    @DisplayName("Testing if 'delete' method returns correct status on missing auth")
    public void testDeleteNotAuthorized() throws Exception {
        this.mockMvc.perform(delete("/api/review/delete/{id}", REVIEW_ID1))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Testing if 'delete' works with user auth")
    public void testDeleteAuthorizedUser() throws Exception {
        this.mockMvc.perform(delete("/api/review/delete/{id}", REVIEW_ID1)
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk());

        assertThat(reviewRepository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("Testing if 'delete' works with admin auth")
    public void testDeleteAuthorizedAdmin() throws Exception {
        this.mockMvc.perform(delete("/api/review/delete/{id}", REVIEW_ID1)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());

        assertThat(reviewRepository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("Testing if 'delete' method returns correct status when entity does not exist")
    public void testDeleteAuthorizedWithUnexistingEntity() throws Exception {
        long count = reviewRepository.count();
        this.mockMvc.perform(delete("/api/review/delete/{id}", count + 1)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Testing if 'delete' method returns correct status when entity_id does not exist")
    public void testDeleteAuthorizedWithUnexistingEntityId() throws Exception {
        this.mockMvc.perform(delete("/api/review/delete/{id}", -1)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound());
    }
    // 'Add' tests

    @Test
    @DisplayName("Testing if 'add' method returns correct status when missing auth")
    public void testAddReviewNotAuthorized() throws Exception {

        ReviewAddRequest reviewAddRequest = new ReviewAddRequest();

        reviewAddRequest.setText(TEXT_1)
                .setProduct_id(productEntity1.getId())
                .setUser_id(userEntity1.getId());

        ObjectMapper mapper = new ObjectMapper();

        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/review/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(reviewAddRequest)))
                .andExpect(status().isUnauthorized());

    }

    @Test
    @DisplayName("Testing if 'add' method works")
    public void testAddReviewAuthorized() throws Exception {

        ReviewAddRequest reviewAddRequest = new ReviewAddRequest();

        reviewAddRequest.setText("Add test text")
                .setProduct_id(productEntity1.getId())
                .setUser_id(userEntity1.getId());

        ObjectMapper objectMapper = new ObjectMapper();

        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/review/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewAddRequest))
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());

        ReviewEntity review = reviewRepository.findReviewEntityByText("Add test text").orElse(null);

        assertThat(review.getText()).isEqualTo("Add test text");

    }

    @Test
    @DisplayName("Testing if 'add' method returns correct status when sent a bad request")
    public void testAddReviewAuthorizedBadRequest() throws Exception {

        ReviewAddRequest reviewAddRequest = new ReviewAddRequest();

        reviewAddRequest.setText(TEXT_1)
                .setProduct_id(productEntity1.getId());
//                .setUser_id(userEntity1.getId());

        ObjectMapper objectMapper = new ObjectMapper();

        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/review/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewAddRequest))
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isBadRequest());

    }

    // 'Update' tests
    @Test
    @DisplayName("Testing if 'update' method returns correct status when missing auth")
    public void testUpdateReviewNotAuthorized() throws Exception {

        ReviewUpdateRequest reviewUpdateRequest = new ReviewUpdateRequest();
        reviewUpdateRequest
                .setId(REVIEW_ID1)
                .setText("update test text")
                .setProduct_id(productEntity1.getId())
                .setUser_id(userEntity1.getId());

        ObjectMapper mapper = new ObjectMapper();

        this.mockMvc.perform(MockMvcRequestBuilders.put("/api/review/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(reviewUpdateRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Testing if 'update' method works")
    public void testUpdateReviewAuthorized() throws Exception {

        ReviewUpdateRequest reviewUpdateRequest = new ReviewUpdateRequest();
        reviewUpdateRequest
                .setId(REVIEW_ID1)
                .setText("update test text")
                .setProduct_id(productEntity1.getId())
                .setUser_id(userEntity1.getId());

        ObjectMapper objectMapper = new ObjectMapper();

        this.mockMvc.perform(MockMvcRequestBuilders.put("/api/review/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewUpdateRequest))
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk());

        ReviewEntity review = reviewRepository.findReviewEntityByText("update test text")
                .orElse(null);

        assertThat(review.getText()).isEqualTo("update test text");

    }

    @Test
    @DisplayName("Testing if 'update' method returns correct status when sent a bad request")
    public void testUpdateReviewAuthorizedBadRequest() throws Exception {

        ReviewUpdateRequest reviewUpdateRequest = new ReviewUpdateRequest();
        reviewUpdateRequest
                .setId(REVIEW_ID1)
                .setText("update test text")
                .setProduct_id(productEntity1.getId());
//                .setUser_id(userEntity1.getId());

        ObjectMapper objectMapper = new ObjectMapper();

        this.mockMvc.perform(MockMvcRequestBuilders.put("/api/review/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewUpdateRequest))
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Testing if 'update' method returns correct status when required entity is not found")
    public void testUpdateReviewAuthorizedNotFound() throws Exception {

        long count = reviewRepository.count();

        ReviewUpdateRequest reviewUpdateRequest = new ReviewUpdateRequest();
        reviewUpdateRequest
                .setId(count + 1)
                .setText("update test text")
                .setProduct_id(productEntity1.getId())
                .setUser_id(userEntity1.getId());

        ObjectMapper objectMapper = new ObjectMapper();

        this.mockMvc.perform(MockMvcRequestBuilders.put("/api/review/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewUpdateRequest))
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound());

    }

    // 'by user' tests
    @Test
    @DisplayName("Testing if 'by user' method returns correct status when missing auth")
    public void testGetReviewsByUserNotAuthorized() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/review/by_user/" + userEntity1.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Testing if 'by user' method returns correct data length when authorized")
    public void testGetReviewsByUserAuthorizedLength() throws Exception {

        this.mockMvc.perform(get("/api/review/by_user/1")
                .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @DisplayName("Testing if 'by user' method returns correct data when authorized")
    public void testGetReviewsByUserAuthorized() throws Exception {

        this.mockMvc.perform(get("/api/review/by_user/1")
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(REVIEW_ID1.intValue())));
    }

    @Test
    @DisplayName("Testing if 'by user' method returns correct status when invalid user id")
    public void testGetReviewsByUserInvalid() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/review/by_user/" + "-1")
                .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Testing if 'by user' method returns correct status when not existing user id")
    public void testGetReviewsByUserNotFound() throws Exception {

        long count = userRepository.count();

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/review/by_user/" + (count + 1))
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isNotFound());
    }

    // 'by product' tests
    @Test
    @DisplayName("Testing if 'by product' method returns correct status when missing auth")
    public void testGetReviewsByProductNotAuthorized() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/review/by_product/" + productEntity1.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Testing if 'by product' method returns correct data length when authorized")
    public void testGetReviewsByProductAuthorizedLength() throws Exception {

        this.mockMvc.perform(get("/api/review/by_product/1")
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @DisplayName("Testing if 'by product' method returns correct data when authorized")
    public void testGetReviewsByProductAuthorized() throws Exception {

        this.mockMvc.perform(get("/api/review/by_product/1")
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(REVIEW_ID1.intValue())));
    }

    @Test
    @DisplayName("Testing if 'by product' method returns correct status when invalid product id")
    public void testGetReviewsByProductInvalid() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/review/by_product/" + "-1")
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Testing if 'by product' method returns correct status when not existing product id")
    public void testGetReviewsByProductNotFound() throws Exception {

        long count = productRepository.count();

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/review/by_product/" + (count + 1))
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isNotFound());
    }

}
