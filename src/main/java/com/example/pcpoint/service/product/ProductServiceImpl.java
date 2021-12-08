package com.example.pcpoint.service.product;

import com.example.pcpoint.exception.ActionForbiddenException;
import com.example.pcpoint.exception.ItemNotFoundException;
import com.example.pcpoint.model.entity.product.ProductEntity;
import com.example.pcpoint.model.entity.product.ProductTypeEntity;
import com.example.pcpoint.model.enums.ProductTypeEnum;
import com.example.pcpoint.model.service.product.ProductAddServiceModel;
import com.example.pcpoint.model.service.product.ProductUpdateServiceModel;
import com.example.pcpoint.repository.product.ProductRepository;
import com.example.pcpoint.repository.product.ProductTypeRepository;
import com.example.pcpoint.service.review.ReviewService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductTypeRepository productTypeRepository;
    private final ReviewService reviewService;

    public ProductServiceImpl(ProductRepository productRepository, ProductTypeRepository productTypeRepository,
                              ReviewService reviewService) {
        this.productRepository = productRepository;
        this.productTypeRepository = productTypeRepository;
        this.reviewService = reviewService;
    }

    @Override
    public void initializeProductsAndTypes() {
        initializeTypes();
        initializeProducts();
    }

    private void initializeProducts() {
        if (productRepository.count() == 0) {
            ProductTypeEntity software = productTypeRepository.findByType(ProductTypeEnum.SOFTWARE)
                    .orElseThrow(() -> new ItemNotFoundException("Product Type with type " + ProductTypeEnum.SOFTWARE + " was not found"));
            ProductTypeEntity hardware = productTypeRepository.findByType(ProductTypeEnum.HARDWARE)
                    .orElseThrow(() -> new ItemNotFoundException("Product Type with type " + ProductTypeEnum.HARDWARE + " was not found"));
            ProductTypeEntity accessory = productTypeRepository.findByType(ProductTypeEnum.ACCESSORY)
                    .orElseThrow(() -> new ItemNotFoundException("Product Type with type " + ProductTypeEnum.ACCESSORY + " was not found"));
            ProductTypeEntity misc = productTypeRepository.findByType(ProductTypeEnum.MISCELLANEOUS)
                    .orElseThrow(() -> new ItemNotFoundException("Product Type with type " + ProductTypeEnum.MISCELLANEOUS + " was not found"));

            ProductEntity softwareEntity = new ProductEntity();
            softwareEntity.setName("Software")
                    .setDescription("Software description")
                    .setPrice(BigDecimal.valueOf(100))
                    .setType(software)
                    .setImageUrl("https://res.cloudinary.com/hanseberg/image/upload/v1638367735/zulg9wkt2q9li6yql9es.jpg")
                    .setQuantity(10)
                    .setTimesBought(0);

            ProductEntity hardwareEntity = new ProductEntity();
            hardwareEntity.setName("Hardware")
                    .setDescription("Hardware description")
                    .setPrice(BigDecimal.valueOf(200))
                    .setType(hardware)
                    .setImageUrl("https://res.cloudinary.com/hanseberg/image/upload/v1638367944/mli36wxftai6lvxvf9wv.jpg")
                    .setQuantity(20)
                    .setTimesBought(0);

            ProductEntity accessoryEntity = new ProductEntity();
            accessoryEntity.setName("Accessory")
                    .setDescription("Accessory description")
                    .setPrice(BigDecimal.valueOf(300))
                    .setType(accessory)
                    .setImageUrl("https://res.cloudinary.com/hanseberg/image/upload/v1638373149/zewuxsw8q8ew3rc4oqsy.jpg")
                    .setQuantity(30)
                    .setTimesBought(0);

            ProductEntity miscEntity = new ProductEntity();
            miscEntity.setName("Misc")
                    .setDescription("Misc description")
                    .setPrice(BigDecimal.valueOf(420))
                    .setType(misc)
                    .setImageUrl("https://res.cloudinary.com/hanseberg/image/upload/v1638367983/bavhgezzl9n69zbvc9lk.jpg")
                    .setQuantity(42)
                    .setTimesBought(0);

            ProductEntity softwareEntity2 = new ProductEntity();
            softwareEntity2.setName("Windows License")
                    .setDescription("A licensed version of Windows OS")
                    .setPrice(BigDecimal.valueOf(420.42))
                    .setType(software)
                    .setImageUrl("https://res.cloudinary.com/hanseberg/image/upload/v1638878987/hqrox67tuewcbghugtp0.jpg")
                    .setQuantity(42)
                    .setTimesBought(0);

            ProductEntity hardwareEntity2 = new ProductEntity();
            hardwareEntity2.setName("Graphics Card")
                    .setDescription("A basic GPU")
                    .setPrice(BigDecimal.valueOf(2000.5))
                    .setType(hardware)
                    .setImageUrl("https://res.cloudinary.com/hanseberg/image/upload/v1638879217/dkbs5ppcgqb8gzysdzyl.jpg")
                    .setQuantity(25)
                    .setTimesBought(0);

            ProductEntity accessoryEntity2 = new ProductEntity();
            accessoryEntity2.setName("Wireless Headphones")
                    .setDescription("Wireless bud headphones")
                    .setPrice(BigDecimal.valueOf(45.55))
                    .setType(accessory)
                    .setImageUrl("https://res.cloudinary.com/hanseberg/image/upload/v1638878859/kqgelcuimgocesu1uoky.jpg")
                    .setQuantity(200)
                    .setTimesBought(0);


            productRepository.save(softwareEntity);
            productRepository.save(hardwareEntity);
            productRepository.save(accessoryEntity);
            productRepository.save(softwareEntity2);
            productRepository.save(hardwareEntity2);
            productRepository.save(accessoryEntity2);
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
    public List<ProductEntity> findAllByTimesBought() {
        return this.productRepository.findAllByTimesBought();
    }

    @Override
    public void addProduct(ProductAddServiceModel productAddServiceModel) {
        ProductEntity productEntity = new ProductEntity();

        productEntity
                .setName(productAddServiceModel.getName())
                .setDescription(productAddServiceModel.getDescription())
                .setImageUrl(productAddServiceModel.getImageUrl())
                .setQuantity(productAddServiceModel.getQuantity())
                .setPrice(productAddServiceModel.getPrice())
                .setTimesBought(0);

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

        if (productRepository.findAllProductsInOrders().contains(id)) {
            throw new ActionForbiddenException("Product Entity with id " + id + " is in an order");
        }

        reviewService.deleteAllReviewsByProductId(id);
        productRepository.deleteProductEntityById(id);
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
