package com.florvia.ecommerce.order;

import com.florvia.ecommerce.entity.Order;
import com.florvia.ecommerce.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderCleanupTask {

    private final OrderRepository orderRepository;

    @Scheduled(fixedRate = 3600000)
    @Transactional
    public void cancelExpiredOrders() {
        log.info("Starting expired orders cleanup task...");

        LocalDateTime expiryTime = LocalDateTime.now().minusHours(24);

        List<Order> expiredOrders = orderRepository.findAllByStatusAndCreatedAtBefore(
                "PENDING_PAYMENT", expiryTime);

        for (Order order : expiredOrders) {
            order.setStatus("CANCELLED");
            log.info("Order ID {} has been cancelled due to payment timeout", order.getId());
        }

        orderRepository.saveAll(expiredOrders);
    }
}