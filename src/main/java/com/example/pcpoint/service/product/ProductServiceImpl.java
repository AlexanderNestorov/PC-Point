package com.example.pcpoint.service.product;

import com.example.pcpoint.exception.ItemNotFoundException;
import com.example.pcpoint.model.entity.product.ProductEntity;
import com.example.pcpoint.model.entity.product.ProductTypeEntity;
import com.example.pcpoint.model.enums.ProductTypeEnum;
import com.example.pcpoint.model.service.product.ProductAddServiceModel;
import com.example.pcpoint.model.service.product.ProductUpdateServiceModel;
import com.example.pcpoint.repository.product.ProductRepository;
import com.example.pcpoint.repository.product.ProductTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductTypeRepository productTypeRepository;

    public ProductServiceImpl(ProductRepository productRepository, ProductTypeRepository productTypeRepository) {
        this.productRepository = productRepository;
        this.productTypeRepository = productTypeRepository;
    }

    @Override
    public void initializeTypes() {
        initializeTypesPrivate();
    }

    private void initializeTypesPrivate() {
            if (productTypeRepository.count() == 0) {
                ProductTypeEntity software = new ProductTypeEntity();
                software.setType(ProductTypeEnum.SOFTWARE);
                productTypeRepository.save(software);

                ProductTypeEntity hardware = new ProductTypeEntity();
                hardware.setType(ProductTypeEnum.HARDWARE);
                productTypeRepository.save(hardware);

                ProductTypeEntity accessory = new ProductTypeEntity();
                accessory.setType(ProductTypeEnum.ACCESSORY);
                productTypeRepository.save(accessory);

                ProductTypeEntity miscellaneous = new ProductTypeEntity();
                miscellaneous.setType(ProductTypeEnum.MISCELLANEOUS);
                productTypeRepository.save(miscellaneous);
        }
    }

    @Override
    public void addProduct(ProductAddServiceModel productAddServiceModel) {
        ProductEntity productEntity = new ProductEntity();

        productEntity
                .setName(productAddServiceModel.getName())
                .setDescription(productAddServiceModel.getDescription())
                .setImageUrl(productAddServiceModel.getImageUrl())
                .setQuantity(productAddServiceModel.getQuantity())
                .setPrice(productAddServiceModel.getPrice());

        String type = productAddServiceModel.getType();


        if (type == null) {
            ProductTypeEntity misc = productTypeRepository.findByType(ProductTypeEnum.MISCELLANEOUS)
                    .orElseThrow(() -> new ItemNotFoundException("Error: Type is not found."));
            productEntity.setType(misc);
        } else {
                switch (type) {
                    case "SOFTWARE":
                        ProductTypeEntity software = productTypeRepository.findByType(ProductTypeEnum.SOFTWARE)
                                .orElseThrow(() -> new ItemNotFoundException("Error: Type is not found."));
                        productEntity.setType(software);

                        break;

                    case "HARDWARE":
                        ProductTypeEntity hardware = productTypeRepository.findByType(ProductTypeEnum.HARDWARE)
                                .orElseThrow(() -> new ItemNotFoundException("Error: Type is not found."));
                        productEntity.setType(hardware);

                        break;

                    case "MISCELLANEOUS":
                        ProductTypeEntity misc = productTypeRepository.findByType(ProductTypeEnum.MISCELLANEOUS)
                                .orElseThrow(() -> new ItemNotFoundException("Error: Type is not found."));
                        productEntity.setType(misc);

                        break;

                    case "ACCESSORY":
                        ProductTypeEntity accessory = productTypeRepository.findByType(ProductTypeEnum.ACCESSORY)
                                .orElseThrow(() -> new ItemNotFoundException("Error: Type is not found."));
                        productEntity.setType(accessory);

                        break;
                }
            }

        productRepository.save(productEntity);
    }

    @Override
    public List<ProductEntity> findAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public ProductEntity findProductById(Long id) {
        return productRepository.findProductById(id)
                .orElseThrow(() -> new ItemNotFoundException("Product Entity with id " + id + " was not found"));
    }

    @Override
    public ProductEntity updateProduct(ProductUpdateServiceModel productUpdateServiceModel) {
        ProductEntity product = productRepository.findById(productUpdateServiceModel.getId())
                .orElse(null);
        if (product == null) {
            return null;
        }


        product
                .setName(productUpdateServiceModel.getName())
                .setDescription(productUpdateServiceModel.getDescription())
                .setPrice(productUpdateServiceModel.getPrice())
                .setQuantity(productUpdateServiceModel.getQuantity())
                .setImageUrl(productUpdateServiceModel.getImageUrl());

        String type = productUpdateServiceModel.getType();


        if (type == null) {
            ProductTypeEntity misc = productTypeRepository.findByType(ProductTypeEnum.MISCELLANEOUS)
                    .orElseThrow(() -> new ItemNotFoundException("Error: Type is not found."));
            product.setType(misc);
        } else {
            switch (type) {
                case "SOFTWARE":
                    ProductTypeEntity software = productTypeRepository.findByType(ProductTypeEnum.SOFTWARE)
                            .orElseThrow(() -> new ItemNotFoundException("Error: Type is not found."));
                    product.setType(software);

                    break;

                case "HARDWARE":
                    ProductTypeEntity hardware = productTypeRepository.findByType(ProductTypeEnum.HARDWARE)
                            .orElseThrow(() -> new ItemNotFoundException("Error: Type is not found."));
                    product.setType(hardware);

                    break;

                case "MISCELLANEOUS":
                    ProductTypeEntity misc = productTypeRepository.findByType(ProductTypeEnum.MISCELLANEOUS)
                            .orElseThrow(() -> new ItemNotFoundException("Error: Type is not found."));
                    product.setType(misc);

                    break;

                case "ACCESSORY":
                    ProductTypeEntity accessory = productTypeRepository.findByType(ProductTypeEnum.ACCESSORY)
                            .orElseThrow(() -> new ItemNotFoundException("Error: Type is not found."));
                    product.setType(accessory);

                    break;
            }
        }

        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

}
