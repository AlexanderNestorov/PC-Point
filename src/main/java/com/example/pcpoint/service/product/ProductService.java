package com.example.pcpoint.service.product;

import com.example.pcpoint.model.entity.product.ProductEntity;
import com.example.pcpoint.model.service.product.ProductAddServiceModel;
import com.example.pcpoint.model.service.product.ProductUpdateServiceModel;

import java.util.List;

public interface ProductService {

    void addProduct(ProductAddServiceModel productAddServiceModel);

    List<ProductEntity> findAllProducts();

    ProductEntity findProductById(Long id);

    ProductEntity findProductByName(String name);

    ProductEntity updateProduct(ProductUpdateServiceModel productUpdateServiceModel);

    void deleteProduct(Long id);

    void initializeProductsAndTypes();

    List<ProductEntity> findAllByTimesBought();

    Boolean existsByName(String name);

}
