package com.florvia.ecommerce.repository;

import com.florvia.ecommerce.entity.Cart;
import com.florvia.ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    // Sửa lỗi dòng 29 & 40
    Optional<Cart> findByUserAndStatus(User user, String status);
}