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

import java.math.BigDecimal;
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
    public void initializeProductsAndTypes() {
        initializeTypes();
        initializeProducts();
    }

    private void initializeProducts() {
        if (productRepository.count() == 0) {
            ProductTypeEntity software = productTypeRepository.findByType(ProductTypeEnum.SOFTWARE).orElse(null);
            ProductTypeEntity hardware = productTypeRepository.findByType(ProductTypeEnum.HARDWARE).orElse(null);
            ProductTypeEntity accessory = productTypeRepository.findByType(ProductTypeEnum.ACCESSORY).orElse(null);
            ProductTypeEntity misc = productTypeRepository.findByType(ProductTypeEnum.MISCELLANEOUS).orElse(null);

            ProductEntity softwareEntity = new ProductEntity();
            softwareEntity.setName("Software")
                    .setDescription("Software description")
                    .setPrice(BigDecimal.valueOf(100))
                    .setType(software)
                    .setImageUrl("software.img")
                    .setQuantity(10);

            ProductEntity hardwareEntity = new ProductEntity();
            hardwareEntity.setName("Hardware")
                    .setDescription("Hardware description")
                    .setPrice(BigDecimal.valueOf(200))
                    .setType(hardware)
                    .setImageUrl("hardware.img")
                    .setQuantity(20);

            ProductEntity accessoryEntity = new ProductEntity();
            accessoryEntity.setName("Accessory")
                    .setDescription("Accessory description")
                    .setPrice(BigDecimal.valueOf(300))
                    .setType(accessory)
                    .setImageUrl("accessory.img")
                    .setQuantity(30);

            ProductEntity miscEntity = new ProductEntity();
            miscEntity.setName("Misc")
                    .setDescription("Misc description")
                    .setPrice(BigDecimal.valueOf(420))
                    .setType(misc)
                    .setImageUrl("misc.img")
                    .setQuantity(42);


            productRepository.save(softwareEntity);
            productRepository.save(hardwareEntity);
            productRepository.save(accessoryEntity);
            productRepository.save(miscEntity);
        }
    }

    private void initializeTypes() {
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

        productEntity.setType(defineType(type));



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
                .orElseThrow(() -> new ItemNotFoundException("Product Entity with id " + productUpdateServiceModel.getId() + " was not found"));
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

        product.setType(defineType(type));


        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public ProductTypeEntity defineType(String type) {

        ProductTypeEntity defined = new ProductTypeEntity();

        if (type == null) {
            defined = productTypeRepository.findByType(ProductTypeEnum.MISCELLANEOUS)
                    .orElseThrow(() -> new ItemNotFoundException("Error: Type is not found."));
        } else {
            switch (type) {
                case "SOFTWARE":
                    defined = productTypeRepository.findByType(ProductTypeEnum.SOFTWARE)
                            .orElseThrow(() -> new ItemNotFoundException("Error: Type is not found."));

                    break;

                case "HARDWARE":
                    defined = productTypeRepository.findByType(ProductTypeEnum.HARDWARE)
                            .orElseThrow(() -> new ItemNotFoundException("Error: Type is not found."));

                    break;

                case "MISCELLANEOUS":
                    defined = productTypeRepository.findByType(ProductTypeEnum.MISCELLANEOUS)
                            .orElseThrow(() -> new ItemNotFoundException("Error: Type is not found."));

                    break;

                case "ACCESSORY":
                    defined = productTypeRepository.findByType(ProductTypeEnum.ACCESSORY)
                            .orElseThrow(() -> new ItemNotFoundException("Error: Type is not found."));

                    break;
            }
        }

        return defined;
    }


}
