package com.florvia.ecommerce.repository;

import com.florvia.ecommerce.entity.Order;
import com.florvia.ecommerce.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    //Order history (Pagination)
    Page<Order> findByUser(User user, Pageable pageable);

    // Search order by status
    List<Order> findAllByStatusAndCreatedAtBefore(String status, LocalDateTime dateTime);
}