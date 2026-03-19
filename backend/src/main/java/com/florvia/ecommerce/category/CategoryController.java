package com.florvia.ecommerce.category;

import com.florvia.ecommerce.category.dto.CategoryResponse;
import com.florvia.ecommerce.category.dto.CreateCategoryRequest;
import com.florvia.ecommerce.common.ApiResponse;
import com.florvia.ecommerce.common.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ApiResponse<PageResponse<CategoryResponse>> getAll(
            @PageableDefault(size = 10) Pageable pageable) {
        return ApiResponse.success("Fetch categories successfully", categoryService.getAll(pageable));
    }

    @PostMapping
    public ApiResponse<Void> create(@Valid @RequestBody CreateCategoryRequest request) {
        categoryService.create(request);
        return ApiResponse.success("Category created successfully", null);
    }
}
