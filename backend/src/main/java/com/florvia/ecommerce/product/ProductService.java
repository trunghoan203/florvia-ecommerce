package com.florvia.ecommerce.product;

import com.florvia.ecommerce.entity.Category;
import com.florvia.ecommerce.entity.Product;
import com.florvia.ecommerce.exception.ResourceNotFoundException;
import com.florvia.ecommerce.product.dto.CreateProductRequest;
import com.florvia.ecommerce.product.dto.ProductResponse;
import com.florvia.ecommerce.repository.CategoryRepository;
import com.florvia.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public void create(CreateProductRequest request) {

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found"));

        Product product = new Product();
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setDescription(request.getDescription());
        product.setStock(request.getStock());
        product.setStatus("ACTIVE");
        product.setCategory(category);

        productRepository.save(product);
    }

    public List<ProductResponse> getAll() {
        return productRepository.findAll().stream()
                .map(p -> new ProductResponse(
                        p.getId(),
                        p.getName(),
                        p.getPrice(),
                        p.getDescription(),
                        p.getStock(),
                        p.getStatus(),
                        p.getCategory().getName()
                ))
                .toList();
    }
}
