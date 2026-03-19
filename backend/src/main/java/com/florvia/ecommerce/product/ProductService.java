package com.florvia.ecommerce.product;

import com.florvia.ecommerce.common.FileStorageService;
import com.florvia.ecommerce.common.PageResponse;
import com.florvia.ecommerce.entity.Category;
import com.florvia.ecommerce.entity.Product;
import com.florvia.ecommerce.entity.ProductImage;
import com.florvia.ecommerce.exception.BadRequestException;
import com.florvia.ecommerce.exception.ResourceNotFoundException;
import com.florvia.ecommerce.product.dto.CreateProductRequest;
import com.florvia.ecommerce.product.dto.ProductResponse;
import com.florvia.ecommerce.product.mapper.ProductMapper;
import com.florvia.ecommerce.repository.CategoryRepository;
import com.florvia.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;
    private final FileStorageService fileStorageService;

    @Transactional
    public void create(CreateProductRequest request) {

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found"));

        Product product = productMapper.toEntity(request);
        product.setStatus("ACTIVE");
        product.setCategory(category);

        productRepository.save(product);
    }

    @Cacheable(value = "products",
            key = "{#name, #categoryId, #minPrice, #maxPrice, #available, #pageable.pageNumber}")
    public PageResponse<ProductResponse> getProducts(
            String name, Long categoryId, Double minPrice, Double maxPrice, Boolean available, Pageable pageable) {

        Specification<Product> spec = Specification.where(ProductSpecification.hasName(name))
                .and(ProductSpecification.hasCategoryId(categoryId))
                .and(ProductSpecification.priceBetween(minPrice, maxPrice))
                .and(ProductSpecification.isAvailable(available));

        Page<Product> productPage = productRepository.findAll(spec, pageable);

        List<ProductResponse> content = productPage.getContent().stream()
                .map(productMapper::toResponse)
                .toList();

        return new PageResponse<>(
                content,
                productPage.getNumber(),
                productPage.getSize(),
                productPage.getTotalElements(),
                productPage.getTotalPages(),
                productPage.isLast()
        );
    }

    @Cacheable(value = "product_detail", key = "#id")
    public ProductResponse getById(Long id) {
        return productRepository.findById(id)
                .map(productMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    @CacheEvict(value = {"products", "product_detail"}, allEntries = true)
    @Transactional
    public void restock(Long productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        product.setStock(product.getStock() + quantity);
        productRepository.save(product);
    }

    @Transactional
    public void uploadProductImages(Long productId, List<MultipartFile> files) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        for (MultipartFile file : files) {
            try {
                String imageUrl = fileStorageService.uploadFile(file);

                ProductImage productImage = new ProductImage();
                productImage.setImageUrl(imageUrl);

                product.addImage(productImage);
            } catch (IOException e) {
                throw new BadRequestException("Failed to upload image: " + file.getOriginalFilename());
            }
        }
        productRepository.save(product);
    }
}