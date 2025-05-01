package ru.sibsutis.payments_service.core.service;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.springframework.stereotype.Service;
import ru.sibsutis.payments_service.core.exception.PaymentAmountTooSmallException;
import ru.sibsutis.payments_service.core.exception.PaymentException;

import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentService {

    public String createPaymentIntent(Long amount, String currency) {
        Map<String, Object> params = new HashMap<>();
        params.put("amount", amount);
        params.put("currency", currency);
        params.put("automatic_payment_methods", Map.of("enabled", true));

        try {
            PaymentIntent intent = PaymentIntent.create(params);
            return intent.getClientSecret();
        } catch (StripeException e) {
            if ("amount_too_small".equals(e.getCode())) {
                throw new PaymentAmountTooSmallException("Amount is too small for payment", e.getRequestId());
            }
            throw new PaymentException("Stripe error: " + e.getMessage(), e.getCode(), e.getRequestId());
        }
    }
}
