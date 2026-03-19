package com.florvia.ecommerce.product;

import com.florvia.ecommerce.entity.Product;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {

    // Filter by name
    public static Specification<Product> hasName(String name) {
        return (root, query, cb) -> name == null ? null : cb.like(root.get("name"), "%" + name + "%");
    }

    // Filter by category
    public static Specification<Product> hasCategoryId(Long categoryId) {
        return (root, query, cb) -> categoryId == null ? null : cb.equal(root.get("category").get("id"), categoryId);
    }

    // Filter by price (Min - Max)
    public static Specification<Product> priceBetween(Double minPrice, Double maxPrice) {
        return (root, query, cb) -> {
            if (minPrice == null && maxPrice == null) return null;
            if (minPrice != null && maxPrice != null) return cb.between(root.get("price"), minPrice, maxPrice);
            if (minPrice != null) return cb.greaterThanOrEqualTo(root.get("price"), minPrice);
            return cb.lessThanOrEqualTo(root.get("price"), maxPrice);
        };
    }

    // Just get available product
    public static Specification<Product> isAvailable(Boolean available) {
        return (root, query, cb) -> (available == null || !available) ? null : cb.greaterThan(root.get("stock"), 0);
    }
}