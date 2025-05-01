package ru.sibsutis.payments_service.api.client.dto;

public record TokenVerifyResponse(
        String login,
        String role,
        String name,
        String surname,
        String email,
        boolean enabled) {}
