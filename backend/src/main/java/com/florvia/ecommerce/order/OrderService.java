package com.florvia.ecommerce.order;

import com.florvia.ecommerce.entity.*;
import com.florvia.ecommerce.exception.BadRequestException;
import com.florvia.ecommerce.exception.ResourceNotFoundException;
import com.florvia.ecommerce.order.dto.CheckoutResponse;
import com.florvia.ecommerce.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Transactional
    public CheckoutResponse checkout(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Cart cart = cartRepository.findByUserAndStatus(user, "ACTIVE")
                .orElseThrow(() -> new BadRequestException("Cart is empty"));

        if (cart.getItems().isEmpty()) {
            throw new BadRequestException("Cart is empty");
        }

        Order order = new Order();
        order.setUser(user);
        order.setStatus("PENDING_PAYMENT");

        double total = 0;

        for (CartItem cartItem : cart.getItems()) {

            Product product = cartItem.getProduct();

            if (product.getStock() < cartItem.getQuantity()) {
                throw new BadRequestException(
                        "Not enough stock for product: " + product.getName());
            }

            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product);

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProductId(product.getId());
            item.setProductName(product.getName());
            item.setPrice(product.getPrice());
            item.setQuantity(cartItem.getQuantity());

            order.getItems().add(item);
            total += product.getPrice() * cartItem.getQuantity();
        }

        order.setTotalAmount(total);
        orderRepository.save(order);

        cart.setStatus("CHECKED_OUT");
        cartRepository.save(cart);

        return new CheckoutResponse(
                order.getId(),
                order.getTotalAmount(),
                order.getStatus()
        );
    }
}
