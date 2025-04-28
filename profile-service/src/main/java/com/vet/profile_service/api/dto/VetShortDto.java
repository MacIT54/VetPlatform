package com.vet.profile_service.api.dto;

public record VetShortDto(
        String id,
        String firstName,
        String lastName,
        String specialization,
        String avatarUrl
) {}
