package com.florvia.ecommerce.category;

import com.florvia.ecommerce.category.dto.CategoryResponse;
import com.florvia.ecommerce.category.dto.CreateCategoryRequest;
import com.florvia.ecommerce.category.mapper.CategoryMapper;
import com.florvia.ecommerce.common.PageResponse;
import com.florvia.ecommerce.entity.Category;
import com.florvia.ecommerce.exception.BadRequestException;
import com.florvia.ecommerce.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Cacheable(value = "categories", key = "#pageable.pageNumber")
    public PageResponse<CategoryResponse> getAll(Pageable pageable) {
        Page<Category> categoryPage = categoryRepository.findAll(pageable);
        List<CategoryResponse> content = categoryPage.getContent().stream()
                .map(categoryMapper::toResponse)
                .toList();

        return new PageResponse<>(
                content,
                categoryPage.getNumber(),
                categoryPage.getSize(),
                categoryPage.getTotalElements(),
                categoryPage.getTotalPages(),
                categoryPage.isLast()
        );
    }

    @CacheEvict(value = "categories", allEntries = true)
    @Transactional
    public void create(CreateCategoryRequest request) {
        if (categoryRepository.existsByName(request.getName())) {
            throw new BadRequestException("Category name already exists");
        }
        Category category = categoryMapper.toEntity(request);
        category.setSlug(request.getName().toLowerCase().replace(" ", "-"));
        categoryRepository.save(category);
    }
}
