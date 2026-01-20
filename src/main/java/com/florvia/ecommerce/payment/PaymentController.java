package com.florvia.ecommerce.payment;

import com.florvia.ecommerce.common.ApiResponse;
import com.florvia.ecommerce.payment.dto.CreatePaymentRequest;
import com.florvia.ecommerce.payment.dto.PaymentResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ApiResponse<PaymentResponse> createPayment(
            @Valid @RequestBody CreatePaymentRequest request) {

        return ApiResponse.success(
                "Create payment success",
                paymentService.createPayment(request)
        );
    }

    // MOCK WEBHOOK
    @PostMapping("/webhook")
    public void webhook(
            @RequestParam Long paymentId,
            @RequestParam boolean success) {

        paymentService.handleWebhook(paymentId, success);
    }
}
