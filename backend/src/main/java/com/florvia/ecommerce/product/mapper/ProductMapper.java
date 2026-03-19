package com.florvia.ecommerce.product.mapper;

import com.florvia.ecommerce.entity.Product;
import com.florvia.ecommerce.product.dto.CreateProductRequest;
import com.florvia.ecommerce.product.dto.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "imageUrls", expression = "java(product.getImages().stream().map(img -> img.getImageUrl()).toList())")
    ProductResponse toResponse(Product product);

    Product toEntity(CreateProductRequest request);
}