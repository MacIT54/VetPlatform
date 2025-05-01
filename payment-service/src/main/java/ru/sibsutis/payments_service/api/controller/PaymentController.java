package ru.sibsutis.payments_service.api.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sibsutis.payments_service.api.dto.PaymentRequest;
import ru.sibsutis.payments_service.core.exception.ForbiddenException;
import ru.sibsutis.payments_service.core.service.PaymentService;

import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/status")
    public ResponseEntity<?> getPaymentStatus(HttpServletRequest request) {
        String login = (String) request.getAttribute("userLogin");
        String role = (String) request.getAttribute("userRole");

        return ResponseEntity.ok(Map.of(
                "message", "Access granted",
                "user", login,
                "role", role
        ));
    }

    @PostMapping
    public ResponseEntity<?> createPayment(@RequestBody PaymentRequest paymentRequest, HttpServletRequest request) {
        String login = (String) request.getAttribute("userLogin");
        String role = (String) request.getAttribute("userRole");

        if (!"USER".equals(role) && !"ADMIN".equals(role)) {
            throw new ForbiddenException("Access denied for role: " + role);
        }

        String clientSecret = paymentService.createPaymentIntent(paymentRequest.amount(), paymentRequest.currency());

        return ResponseEntity.ok(Map.of(
                "clientSecret", clientSecret,
                "user", login,
                "role", role
        ));
    }
}
