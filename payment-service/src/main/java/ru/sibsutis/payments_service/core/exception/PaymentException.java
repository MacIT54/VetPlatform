package ru.sibsutis.payments_service.core.exception;

public class PaymentException extends RuntimeException {
    private final String code;
    private final String requestId;

    public PaymentException(String message, String code, String requestId) {
        super(message);
        this.code = code;
        this.requestId = requestId;
    }

    public String getCode() {
        return code;
    }

    public String getRequestId() {
        return requestId;
    }
}

