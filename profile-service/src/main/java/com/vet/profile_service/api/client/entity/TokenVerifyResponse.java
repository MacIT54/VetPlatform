package com.vet.profile_service.api.client.entity;

public record TokenVerifyResponse(
        String username,
        String role,
        String name,
        String surname,
        String email,
        boolean isEnabled
) {}