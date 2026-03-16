package com.florvia.ecommerce.product;

import com.florvia.ecommerce.common.ApiResponse;
import com.florvia.ecommerce.product.dto.CreateProductRequest;
import com.florvia.ecommerce.product.dto.ProductResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // PUBLIC
    @GetMapping
    public ApiResponse<List<ProductResponse>> getAll() {
        return ApiResponse.success(
                "Get products success",
                productService.getAll()
        );
    }

    // ADMIN
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ApiResponse<Void> create(
            @Valid @RequestBody CreateProductRequest request) {

        productService.create(request);
        return ApiResponse.success("Create product success", null);
    }
}
