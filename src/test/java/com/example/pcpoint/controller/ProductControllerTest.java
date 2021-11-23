package com.example.pcpoint.controller;

import com.example.pcpoint.exception.ItemNotFoundException;
import com.example.pcpoint.model.entity.product.ProductEntity;
import com.example.pcpoint.model.entity.product.ProductTypeEntity;
import com.example.pcpoint.model.request.product.ProductAddRequest;
import com.example.pcpoint.model.request.product.ProductUpdateRequest;
import com.example.pcpoint.model.response.JwtResponse;
import com.example.pcpoint.model.service.user.UserLoginServiceModel;
import com.example.pcpoint.repository.product.ProductRepository;
import com.example.pcpoint.repository.product.ProductTypeRepository;
import com.example.pcpoint.repository.user.UserRepository;
import com.example.pcpoint.service.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductTypeRepository productTypeRepository;

    @Autowired
    private UserService userService;

    private Long PRODUCT_ID1, PRODUCT_ID2;

    private String NAME1 = "Product1", NAME2 = "Product2";

    private String DESCRIPTION_1 = "product text 1", DESCRIPTION_2 = "product text 2";

    private String IMAGE_1 = "image_url1", IMAGE_2 = "image_url2";

    private int QUANTITY_1 = 10, QUANTITY_2 = 20;

    private BigDecimal PRICE_1 = BigDecimal.valueOf(100), PRICE_2 = BigDecimal.valueOf(200);

    private ProductTypeEntity type1, type2;

    private String userToken, adminToken;

    @BeforeEach
    public void setUp(){

        productRepository.deleteAll();

        UserLoginServiceModel userLoginServiceModel = new UserLoginServiceModel();
        userLoginServiceModel.setUsername("admin");
        userLoginServiceModel.setPassword("1234");

        JwtResponse jwtResponseAdmin = userService.loginUser(userLoginServiceModel);

        adminToken = jwtResponseAdmin.getAccessToken();

        userLoginServiceModel.setUsername("user");
        userLoginServiceModel.setPassword("1234");

        JwtResponse jwtResponseUser = userService.loginUser(userLoginServiceModel);

        userToken = jwtResponseUser.getAccessToken();

        type1 = productTypeRepository.findById(1L)
                .orElseThrow(() -> new ItemNotFoundException("Product type not found"));

        type2 = productTypeRepository.findById(2L)
                .orElseThrow(() -> new ItemNotFoundException("Product type not found"));

        ProductEntity productEntity1 = new ProductEntity();

        productEntity1.setName(NAME1)
                .setDescription(DESCRIPTION_1)
                .setImageUrl(IMAGE_1)
                .setQuantity(QUANTITY_1)
                .setPrice(PRICE_1)
                .setType(type1);

        PRODUCT_ID1 = productRepository.save(productEntity1).getId();

        ProductEntity productEntity2 = new ProductEntity();

        productEntity2.setName(NAME2)
                .setDescription(DESCRIPTION_2)
                .setImageUrl(IMAGE_2)
                .setQuantity(QUANTITY_2)
                .setPrice(PRICE_2)
                .setType(type2);

        PRODUCT_ID2 = productRepository.save(productEntity2).getId();
    }

    @AfterEach
    public void tearDown(){
        productRepository.deleteAll();
    }

    @Test
    @DisplayName("Testing if testing components load successfully")
    public void contextLoads(){
        assertThat(productRepository).isNotNull();
        assertThat(mockMvc).isNotNull();
        assertThat(userService).isNotNull();
        assertThat(userRepository).isNotNull();
        assertThat(productRepository).isNotNull();
    }

    // 'All' tests

    @Test
    @DisplayName("Test if 'all' method status is ok")
    public void testProductsReturnsCorrectStatusCode() throws Exception {
        this.mockMvc.perform(get("/api/product/all"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Testing if 'all' method returns correct content type")
    public void testProductsReturnsCorrectContentType() throws Exception {
        this.mockMvc.perform(get("/api/product/all"))
                .andExpect(content().contentType("application/json"));
    }

    @Test
    @DisplayName("Testing if 'all' method returns correct number of products")
    public void testProductsReturnsCorrectNumberOfEntries() throws Exception {
        this.mockMvc.perform(get("/api/product/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @DisplayName("Testing if 'all' method returns correct products")
    public void testProductsReturnsCorrectEntries() throws Exception {
        this.mockMvc.perform(get("/api/product/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(PRODUCT_ID1.intValue())))
                .andExpect(jsonPath("$[1].id", is(PRODUCT_ID2.intValue())));
    }

    // 'Find' tests

    @Test
    @DisplayName("Testing if 'find' method returns correct products")
    public void testFindProductReturnsCorrectEntity() throws Exception {
        this.mockMvc.perform(get("/api/product/find/{id}", PRODUCT_ID1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(PRODUCT_ID1.intValue())));
    }

    @Test
    @DisplayName("Testing if 'find' returns correct status when entity is not found")
    public void testFindProductWhenNonExistingEntityId() throws Exception {
        this.mockMvc.perform(get("/api/product/find/{id}", -1))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Testing if 'find' returns correct status when entity_id is not found")
    public void testFindProductWhenNonExistingEntity() throws Exception {
        long count = productRepository.count();
        this.mockMvc.perform(get("/api/product/find/{id}", count + 1))
                .andExpect(status().isNotFound());
    }

    // 'Delete' tests
    @Test
    @DisplayName("Testing if 'delete' method returns correct status on missing auth")
    public void testDeleteNotAuthorized() throws Exception {
        this.mockMvc.perform(delete("/api/product/delete/{id}", PRODUCT_ID1))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Testing if 'delete' method returns correct status on wrong user role")
    public void testDeleteNotAuthorizedWithWrongRole() throws Exception {
        this.mockMvc.perform(delete("/api/product/delete/{id}", PRODUCT_ID1)
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Testing if 'delete' works with correct auth")
    public void testDeleteAuthorized() throws Exception {
        this.mockMvc.perform(delete("/api/product/delete/{id}", PRODUCT_ID1)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());

        assertThat(productRepository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("Testing if 'delete' method returns correct status when entity does not exist")
    public void testDeleteAuthorizedWithUnexistingEntity() throws Exception {
        long count = productRepository.count();
        this.mockMvc.perform(delete("/api/product/delete/{id}", count + 1)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Testing if 'delete' method returns correct status when entity_id does not exist")
    public void testDeleteAuthorizedWithUnexistingEntityId() throws Exception {
        this.mockMvc.perform(delete("/api/product/delete/{id}", -1)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound());
    }

    // 'Add' tests
    @Test
    @DisplayName("Testing if 'add' method returns correct status when missing auth")
    public void testAddProductNotAuthorized() throws Exception {

        ProductAddRequest productAddRequest = new ProductAddRequest();
        productAddRequest.setName("Add name")
                .setDescription("Add description")
                .setPrice(BigDecimal.valueOf(420))
                .setQuantity(420)
                .setImageUrl("addimage.url")
                .setType(type1.getType().name());

        ObjectMapper mapper = new ObjectMapper();

        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/product/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(productAddRequest))
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isForbidden());

    }

    @Test
    @DisplayName("Testing if 'add' method works")
    public void testAddProductAuthorized() throws Exception {

        ProductAddRequest productAddRequest = new ProductAddRequest();
        productAddRequest.setName("Add name")
                .setDescription("Add description")
                .setPrice(BigDecimal.valueOf(420))
                .setQuantity(420)
                .setImageUrl("addimage.url")
                .setType(type1.getType().name());

        ObjectMapper objectMapper = new ObjectMapper();

        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/product/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productAddRequest))
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());

        ProductEntity product = productRepository.findProductByName("Add name")
                .orElseThrow(() -> new ItemNotFoundException("Product not found"));

        assertThat(product.getName()).isEqualTo("Add name");

    }

    @Test
    @DisplayName("Testing if 'add' method returns correct status when sent a bad request")
    public void testAddProductAuthorizedBadRequest() throws Exception {

        ProductAddRequest productAddRequest = new ProductAddRequest();
        productAddRequest.setName("Add name")
                .setDescription("Add description")
                .setPrice(BigDecimal.valueOf(420))
                .setQuantity(420)
                .setImageUrl("addimage.url");
//                .setType(type1.getType().name());

        ObjectMapper objectMapper = new ObjectMapper();

        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/product/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productAddRequest))
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isBadRequest());

    }

    // 'Update' tests
    @Test
    @DisplayName("Testing if 'update' method returns correct status when missing auth")
    public void testUpdateProductNotAuthorized() throws Exception {

        ProductUpdateRequest productUpdateRequest = new ProductUpdateRequest();
        productUpdateRequest.setId(PRODUCT_ID1)
                .setName("Update name")
                .setDescription("Update description")
                .setPrice(BigDecimal.valueOf(420))
                .setQuantity(420)
                .setImageUrl("updateimage.url")
                .setType(type1.getType().name());


        ObjectMapper mapper = new ObjectMapper();

        this.mockMvc.perform(MockMvcRequestBuilders.put("/api/product/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(productUpdateRequest))
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Testing if 'update' method works")
    public void testUpdateProductAuthorized() throws Exception {

        ProductUpdateRequest productUpdateRequest = new ProductUpdateRequest();
        productUpdateRequest.setId(PRODUCT_ID1)
                .setName("Update name")
                .setDescription("Update description")
                .setPrice(BigDecimal.valueOf(420))
                .setQuantity(420)
                .setImageUrl("updateimage.url")
                .setType(type1.getType().name());


        ObjectMapper objectMapper = new ObjectMapper();

        this.mockMvc.perform(MockMvcRequestBuilders.put("/api/product/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productUpdateRequest))
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());

        ProductEntity product = productRepository.findProductByName("Update name")
                .orElseThrow(() -> new ItemNotFoundException("Product not found"));

        assertThat(product.getName()).isEqualTo("Update name");

    }

    @Test
    @DisplayName("Testing if 'update' method returns correct status when sent a bad request")
    public void testUpdateProductAuthorizedBadRequest() throws Exception {

        ProductUpdateRequest productUpdateRequest = new ProductUpdateRequest();
        productUpdateRequest.setId(PRODUCT_ID1)
                .setName("Update name")
                .setDescription("Update description")
                .setPrice(BigDecimal.valueOf(420))
                .setQuantity(420)
                .setImageUrl("updateimage.url");
//                .setType(type1.getType().name());

        ObjectMapper objectMapper = new ObjectMapper();

        this.mockMvc.perform(MockMvcRequestBuilders.put("/api/product/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productUpdateRequest))
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isBadRequest());

    }

    @Test
    @DisplayName("Testing if 'update' method returns correct status when required entity is not found")
    public void testUpdateProductAuthorizedNotFound() throws Exception {

        long count = productRepository.count();

        ProductUpdateRequest productUpdateRequest = new ProductUpdateRequest();
        productUpdateRequest.setId(count + 1)
                .setName("Update name")
                .setDescription("Update description")
                .setPrice(BigDecimal.valueOf(420))
                .setQuantity(420)
                .setImageUrl("updateimage.url")
                .setType(type1.getType().name());

        ObjectMapper objectMapper = new ObjectMapper();

        this.mockMvc.perform(MockMvcRequestBuilders.put("/api/product/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productUpdateRequest))
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound());

    }
}
