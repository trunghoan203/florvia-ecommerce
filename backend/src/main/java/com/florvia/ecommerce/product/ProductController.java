package com.florvia.ecommerce.product;

import com.florvia.ecommerce.common.ApiResponse;
import com.florvia.ecommerce.common.PageResponse;
import com.florvia.ecommerce.exception.BadRequestException;
import com.florvia.ecommerce.product.dto.CreateProductRequest;
import com.florvia.ecommerce.product.dto.ProductResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ApiResponse<PageResponse<ProductResponse>> getAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Boolean available,
            @PageableDefault(size = 12, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        return ApiResponse.success(
                "Fetch products successfully",
                productService.getProducts(name, categoryId, minPrice, maxPrice, available, pageable)
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

    @PutMapping("/{id}/restock")
    public ApiResponse<Void> restock(@PathVariable Long id, @RequestParam Integer quantity) {
        productService.restock(id, quantity);
        return ApiResponse.success("Stock updated successfully", null);
    }

    @PostMapping("/{id}/images")
    public ApiResponse<String> uploadImages(
            @PathVariable Long id,
            @RequestParam("files") List<MultipartFile> files) {

        if (files == null || files.isEmpty()) {
            throw new BadRequestException("Please select at least one image to upload");
        }

        productService.uploadProductImages(id, files);
        return ApiResponse.success("Upload images successfully", null);
    }
}
