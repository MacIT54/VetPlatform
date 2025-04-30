package com.vet.appointment_service.appointment_service.api.dto;

public record VetShortDto(
        String id,
        String firstName,
        String lastName,
        String specialization,
        String avatarUrl
) {}
