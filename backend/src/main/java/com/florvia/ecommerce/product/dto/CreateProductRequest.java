package com.florvia.ecommerce.product.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateProductRequest {
    @NotBlank(message = "The product name cannot be blank")
    @Size(min = 5, max = 100, message = "Name must be between 5 and 100 characters")
    private String name;

    @Positive(message = "The price must be greater than 0")
    private double price;

    @Min(value = 0, message = "Inventory must not be negative")
    private int stock;

    @NotNull(message = "The category must not be blank.")
    private Long categoryId;

    private String description;
}