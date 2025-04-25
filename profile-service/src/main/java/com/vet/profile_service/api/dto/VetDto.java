package com.vet.profile_service.api.dto;

import jakarta.annotation.Nullable;

import java.util.List;

public record VetDto(
        String id,
        String firstName,
        String lastName,
        String specialization,
        String qualification,
        String avatarUrl,
        @Nullable String clinicName,
        AddressDto address,
        double rating,
        List<String> services
) {
    public record AddressDto(
            String city,
            String street
    ) {}
}
