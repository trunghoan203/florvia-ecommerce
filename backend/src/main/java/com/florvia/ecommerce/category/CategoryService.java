package com.florvia.ecommerce.category;

import com.florvia.ecommerce.category.dto.CategoryResponse;
import com.florvia.ecommerce.category.dto.CreateCategoryRequest;
import com.florvia.ecommerce.entity.Category;
import com.florvia.ecommerce.exception.BadRequestException;
import com.florvia.ecommerce.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public void create(CreateCategoryRequest request) {

        if (categoryRepository.existsByName(request.getName())) {
            throw new BadRequestException("Category already exists");
        }

        Category category = new Category();
        category.setName(request.getName());
        category.setSlug(request.getName().toLowerCase().replace(" ", "-"));
        category.setStatus("ACTIVE");

        categoryRepository.save(category);
    }

    public List<CategoryResponse> getAll() {
        return categoryRepository.findAll().stream()
                .map(c -> new CategoryResponse(
                        c.getId(),
                        c.getName(),
                        c.getSlug(),
                        c.getStatus()
                ))
                .toList();
    }
}
