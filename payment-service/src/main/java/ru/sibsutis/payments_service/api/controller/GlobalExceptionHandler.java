package ru.sibsutis.payments_service.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.sibsutis.payments_service.core.exception.ForbiddenException;
import ru.sibsutis.payments_service.core.exception.PaymentAmountTooSmallException;
import ru.sibsutis.payments_service.core.exception.PaymentException;
import ru.sibsutis.payments_service.core.exception.UnauthorizedException;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<Map<String, Object>> handlePaymentException(PaymentException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "error", ex.getMessage(),
                "code", ex.getCode(),
                "requestId", ex.getRequestId()
        ));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Map<String, String>> handleUnauthorized(UnauthorizedException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<Map<String, String>> handleForbidden(ForbiddenException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Unexpected error occurred: " + ex.getMessage()));
    }

    @ExceptionHandler(PaymentAmountTooSmallException.class)
    public ResponseEntity<Map<String, Object>> handlePaymentAmountTooSmall(PaymentAmountTooSmallException ex) {
        return ResponseEntity.badRequest().body(Map.of(
                "error", ex.getMessage(),
                "requestId", ex.getRequestId()
        ));
    }
}

