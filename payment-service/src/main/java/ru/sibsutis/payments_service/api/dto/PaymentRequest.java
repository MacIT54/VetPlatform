package ru.sibsutis.payments_service.api.dto;

public record PaymentRequest(Long amount, String currency, String description) {}
