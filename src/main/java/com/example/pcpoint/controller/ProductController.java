package com.example.pcpoint.controller;

import com.example.pcpoint.model.entity.product.ProductEntity;
import com.example.pcpoint.model.request.product.ProductAddRequest;
import com.example.pcpoint.model.request.product.ProductUpdateRequest;
import com.example.pcpoint.model.response.MessageResponse;
import com.example.pcpoint.model.service.product.ProductAddServiceModel;
import com.example.pcpoint.model.service.product.ProductUpdateServiceModel;
import com.example.pcpoint.service.product.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final ModelMapper modelMapper;
    private final ProductService productService;

    public ProductController(ModelMapper modelMapper, ProductService productService) {
        this.modelMapper = modelMapper;
        this.productService = productService;
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllProducts() {
        List<ProductEntity> products = this.productService.findAllProducts();
        return ResponseEntity.ok(products);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addProduct(@Valid @RequestBody ProductAddRequest productAddRequest,
                                        BindingResult bindingResult) {


        if(bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid product request data!"));
        }

        ProductAddServiceModel productAddServiceModel =
                modelMapper.map(productAddRequest, ProductAddServiceModel.class);


        productService.addProduct(productAddServiceModel);


        return ResponseEntity.ok(new MessageResponse("Product added successfully!"));
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateProduct(@Valid @RequestBody ProductUpdateRequest productUpdateRequest,
                                           BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid product request data!"));
        }

        ProductUpdateServiceModel productUpdateServiceModel =
                modelMapper.map(productUpdateRequest, ProductUpdateServiceModel.class);

        ProductEntity updated = productService.updateProduct(productUpdateServiceModel);


        return ResponseEntity.ok(updated);

    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") Long id) {

        Long deletionId = productService.findProductById(id).getId();

        if(deletionId == null){
            return ResponseEntity.notFound().build();
        }

        this.productService.deleteProduct(id);

        return ResponseEntity.ok(new MessageResponse("Product deleted successfully!"));
    }
}
