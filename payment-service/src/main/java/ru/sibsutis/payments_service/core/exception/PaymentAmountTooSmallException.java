package ru.sibsutis.payments_service.core.exception;

public class PaymentAmountTooSmallException extends RuntimeException {
    private final String requestId;

    public PaymentAmountTooSmallException(String message, String requestId) {
        super(message);
        this.requestId = requestId;
    }

    public String getRequestId() {
        return requestId;
    }
}

