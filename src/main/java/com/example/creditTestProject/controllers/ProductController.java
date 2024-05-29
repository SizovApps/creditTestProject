package com.example.creditTestProject.controllers;


import com.example.creditTestProject.models.Product;
import com.example.creditTestProject.responses.ProductResponse;
import com.example.creditTestProject.services.ProductService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("api/v1/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @GetMapping
    public ResponseEntity<ProductResponse> getProducts() {
        List<Product> products = productService.getProducts();
        return new ResponseEntity<>(new ProductResponse(200, products, null), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Long id) {
        Product product = productService.getProduct(id);
        if (product == null) {
            return new ResponseEntity<>(new ProductResponse(204, null, null), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(new ProductResponse(200, product, null), HttpStatus.OK);
    }

    @Validated
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody Product product) {
        Product new_product = productService.createProduct(product);
        return new ResponseEntity<>(new ProductResponse(201, new_product, null), HttpStatus.CREATED);

    }

    @PutMapping
    public ResponseEntity<ProductResponse> updateProduct(@Valid @RequestBody Product product) {
        try {
            Product new_product = productService.updateProduct(product);
            return new ResponseEntity<>(new ProductResponse(200, new_product, null), HttpStatus.OK);
        } catch (IllegalArgumentException exception) {
            return new ResponseEntity<>(new ProductResponse(404, null, exception.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProductResponse> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return new ResponseEntity<>(new ProductResponse(200, null, null), HttpStatus.OK);
        } catch (IllegalArgumentException exception) {
            return new ResponseEntity<>(new ProductResponse(404, null, exception.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

}
