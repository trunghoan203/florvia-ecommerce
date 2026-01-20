package com.florvia.ecommerce.payment;

import com.florvia.ecommerce.entity.Order;
import com.florvia.ecommerce.entity.Payment;
import com.florvia.ecommerce.exception.BadRequestException;
import com.florvia.ecommerce.exception.ResourceNotFoundException;
import com.florvia.ecommerce.payment.dto.CreatePaymentRequest;
import com.florvia.ecommerce.payment.dto.PaymentResponse;
import com.florvia.ecommerce.repository.OrderRepository;
import com.florvia.ecommerce.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    public PaymentResponse createPayment(CreatePaymentRequest request) {

        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (!order.getStatus().equals("PENDING_PAYMENT")) {
            throw new BadRequestException("Order is not ready for payment");
        }

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(order.getTotalAmount());
        payment.setProvider(request.getProvider());
        payment.setStatus("INIT");

        paymentRepository.save(payment);

        // MOCK PAYMENT URL
        String paymentUrl = "https://mock-payment-gateway.com/pay?paymentId="
                + payment.getId();

        return new PaymentResponse(
                payment.getId(),
                paymentUrl,
                payment.getStatus()
        );
    }

    public void handleWebhook(Long paymentId, boolean success) {

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        Order order = payment.getOrder();

        if (success) {
            payment.setStatus("SUCCESS");
            order.setStatus("PAID");
        } else {
            payment.setStatus("FAILED");
            order.setStatus("FAILED");
        }

        paymentRepository.save(payment);
        orderRepository.save(order);
    }
}
