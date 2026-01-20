package com.florvia.ecommerce.category;

import com.florvia.ecommerce.category.dto.CategoryResponse;
import com.florvia.ecommerce.category.dto.CreateCategoryRequest;
import com.florvia.ecommerce.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    // PUBLIC
    @GetMapping
    public ApiResponse<List<CategoryResponse>> getAll() {
        return ApiResponse.success(
                "Get categories success",
                categoryService.getAll()
        );
    }

    // ADMIN
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ApiResponse<Void> create(
            @Valid @RequestBody CreateCategoryRequest request) {

        categoryService.create(request);
        return ApiResponse.success("Create category success", null);
    }
}
