package com.example.creditTestProject.services;


import com.example.creditTestProject.models.Product;
import java.util.List;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

public interface ProductService {

    List<Product> getProducts();

    Product getProduct(@PathVariable Long id);

    Product createProduct(@ModelAttribute Product product);

    Product updateProduct(@ModelAttribute Product product);

    void deleteProduct(@PathVariable Long id);
}
