package com.vet.profile_service.api.dto;

public record PetShortDto(
        String id,
        String name,
        PetType type
) {}
