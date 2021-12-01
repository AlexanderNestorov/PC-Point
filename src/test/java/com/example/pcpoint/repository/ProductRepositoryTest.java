package com.example.pcpoint.repository;

import com.example.pcpoint.exception.ItemNotFoundException;
import com.example.pcpoint.model.entity.product.ProductEntity;
import com.example.pcpoint.repository.product.ProductRepository;
import com.example.pcpoint.repository.product.ProductTypeRepository;
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
public class ProductRepositoryTest {

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
        assertThat(productRepository).isNotNull();
    }

    @Test
    @DisplayName("Testing if get method returns correct amount of products")
    public void testGetAllMethod() {
        assertThat(productRepository.findAll().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Testing if add method works correctly")
    public void testAdd() {
        assertThat(productRepository.findAll().size()).isEqualTo(2);
        assertThat(productRepository.findById(productEntity1.getId()).get().getName()).isEqualTo("Test Product");
    }

    @Test
    @DisplayName("Testing if find by id method works correctly")
    public void testFindById() {
        ProductEntity test = productRepository.findById(productEntity2.getId()).orElseThrow(
                () -> new ItemNotFoundException("Product not found")
        );
        assertThat(test).isNotNull();
        assertThat(test.getId()).isEqualTo(productEntity2.getId());
    }

    @Test
    @DisplayName("Testing if find by name method works correctly")
    public void testFindByName() {
        ProductEntity test = productRepository.findProductByName(productEntity1.getName()).orElseThrow(
                () -> new ItemNotFoundException("Product not found")
        );
        assertThat(test).isNotNull();
        assertThat(test.getName()).isEqualTo(productEntity1.getName());
        assertThat(test.getId()).isEqualTo(productEntity1.getId());
    }

    @Test
    @DisplayName("Testing if delete by id method works correctly")
    public void testDeleteById() {
        ProductEntity test = productRepository.findById(productEntity2.getId()).orElse(null);
        assertThat(test).isNotNull();

        productRepository.deleteById(test.getId());

        assertThat(productRepository.count()).isEqualTo(1);
        assertThat(productRepository.findById(productEntity2.getId()).orElse(null)).isNull();
    }

    @Test
    @DisplayName("Testing if find products in orders method works correctly")
    public void testFindProductsInOrders() {
        List<Long> test = productRepository.findAllProductsInOrders();
        assertThat(test).isNotNull();
        assertThat(test.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("Testing if find products in orders method works correctly")
    public void testAllByTimesBought() {
        List<ProductEntity> test = productRepository.findAllByTimesBought();
        assertThat(test).isNotNull();
        assertThat(test.size()).isEqualTo(2);
        assertThat(test.get(0).getTimesBought()).isEqualTo(1);
        assertThat(test.get(1).getTimesBought()).isEqualTo(2);
    }
}
