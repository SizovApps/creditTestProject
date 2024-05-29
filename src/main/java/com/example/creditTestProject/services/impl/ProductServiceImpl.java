package com.example.creditTestProject.services.impl;


import com.example.creditTestProject.models.Product;
import com.example.creditTestProject.repositories.ProductRepository;
import com.example.creditTestProject.services.ProductService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProduct(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Product product) {
        if (product.getId() != null && getProduct(product.getId()) != null) {
            return productRepository.save(product);
        }
        throw new IllegalArgumentException("Product with such id not found!");
    }

    @Override
    public void deleteProduct(Long id) {
        if (getProduct(id) != null) {
            productRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Product with such id not found!");
        }
    }
}
