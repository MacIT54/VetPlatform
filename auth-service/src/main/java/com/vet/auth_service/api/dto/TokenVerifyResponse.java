package com.vet.auth_service.api.dto;

public record TokenVerifyResponse(
        String login,
        String role,
        String name,
        String surname,
        String email,
        boolean enabled) {
}
