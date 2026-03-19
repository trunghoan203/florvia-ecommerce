package com.florvia.ecommerce.category.mapper;

import com.florvia.ecommerce.entity.Category;
import com.florvia.ecommerce.category.dto.CategoryResponse;
import com.florvia.ecommerce.category.dto.CreateCategoryRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryResponse toResponse(Category category);
    Category toEntity(CreateCategoryRequest request);
}