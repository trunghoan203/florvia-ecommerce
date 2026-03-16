package com.florvia.ecommerce.repository;

import com.florvia.ecommerce.entity.Cart;
import com.florvia.ecommerce.entity.CartItem;
import com.florvia.ecommerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    // Sửa lỗi dòng 47
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
}