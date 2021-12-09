package com.example.pcpoint.service;

import com.example.pcpoint.exception.ItemNotFoundException;
import com.example.pcpoint.model.entity.product.ProductEntity;
import com.example.pcpoint.model.service.product.ProductAddServiceModel;
import com.example.pcpoint.model.service.product.ProductUpdateServiceModel;
import com.example.pcpoint.repository.product.ProductRepository;
import com.example.pcpoint.repository.product.ProductTypeRepository;
import com.example.pcpoint.service.product.ProductService;
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
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductTypeRepository productTypeRepository;

    ProductEntity productEntity1, productEntity2;

    @BeforeEach
    public void setUp() {
        productRepository.deleteAll();

        ProductEntity testEntity1 = new ProductEntity();
        testEntity1.setName("Test Product")
                .setPrice(BigDecimal.valueOf(100))
                .setType(productTypeRepository.findById(1L).orElseThrow(
                        () -> new ItemNotFoundException("Product type not found")
                ));
        testEntity1
                .setDescription("Test description")
                .setImageUrl("http://test.com/test.jpg")
                .setQuantity(10)
                .setTimesBought(1);

        ProductEntity testEntity2 = new ProductEntity();
        testEntity2.setName("Test Product 2")
                .setPrice(BigDecimal.valueOf(150))
                .setType(productTypeRepository.findById(2L).orElseThrow(
                        () -> new ItemNotFoundException("Product type not found")
                ));
        testEntity2
                .setDescription("Test description 2")
                .setImageUrl("http://test.com/test2.jpg")
                .setQuantity(10)
                .setTimesBought(2);

        productEntity1 = productRepository.save(testEntity1);
        productEntity2 = productRepository.save(testEntity2);

    }

    @AfterEach
    public void tearDown() {
        productRepository.deleteAll();
    }



    @Test
    @DisplayName("Testing if testing components load successfully")
    public void contextLoads() {
        assertThat(productService).isNotNull();
        assertThat(productRepository).isNotNull();
        assertThat(productTypeRepository).isNotNull();
    }

    @Test
    @DisplayName("Testing if get method returns correct amount of products")
    public void testGetAllMethod() {
        assertThat(productService.findAllProducts().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Testing if add method works correctly")
    public void testAdd() {
        ProductAddServiceModel productAddServiceModel = new ProductAddServiceModel();

        assertThat(productService.findAllProducts().size()).isEqualTo(2);

        productAddServiceModel.setName("Test")
                .setPrice(new BigDecimal("1.00"))
                .setDescription("Test desc")
                .setType("HARDWARE")
                .setQuantity(1)
                .setImageUrl("https://www.google.com/");

        productService.addProduct(productAddServiceModel);



        assertThat(productService.findAllProducts().size()).isEqualTo(3);
    }

    @Test
    @DisplayName("Testing if update method works correctly")
    public void testUpdate() {
        ProductUpdateServiceModel productUpdateServiceModel = new ProductUpdateServiceModel();

        productUpdateServiceModel
                .setId(productEntity1.getId())
                .setName("Test-Updated")
                .setPrice(new BigDecimal("1.00"))
                .setDescription("Test desc")
                .setType("HARDWARE")
                .setQuantity(1)
                .setImageUrl("https://www.google.com/");

        productService.updateProduct(productUpdateServiceModel);

        assertThat(productService.findAllProducts().size()).isEqualTo(2);
        assertThat(productService.findProductById(productEntity1.getId()).getName()).isEqualTo("Test-Updated");
    }

    @Test
    @DisplayName("Testing if find by times bought method works correctly")
    public void testFindByTimesBought() {
        List<ProductEntity> list = productService.findAllByTimesBought();
        assertThat(list.size()).isEqualTo(2);
        assertThat(list.get(0).getTimesBought()).isEqualTo(2);
        assertThat(list.get(1).getTimesBought()).isEqualTo(1);
    }

    @Test
    @DisplayName("Testing if find by id method works correctly")
    public void testFindById() {
        ProductEntity test = productService.findProductById(productEntity1.getId());
        assertThat(test).isNotNull();
        assertThat(test.getId()).isEqualTo(productEntity1.getId());
    }

    @Test
    @DisplayName("Testing if find by name method works correctly")
    public void testFindByName() {
        ProductEntity test = productService.findProductByName(productEntity1.getName());
        assertThat(test).isNotNull();
        assertThat(test.getName()).isEqualTo(productEntity1.getName());
    }

    @Test
    @DisplayName("Testing if delete by id method works correctly")
    public void testDeleteById() {
       productService.deleteProduct(productEntity1.getId());

       assertThat(productService.findAllProducts().size()).isEqualTo(1);
    }
}
