package com.florvia.ecommerce.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateProductRequest {

    @NotBlank
    private String name;

    @NotNull
    private Double price;

    private String description;

    @NotNull
    private Integer stock;

    @NotNull
    private Long categoryId;
}
