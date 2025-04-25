package com.vet.profile_service.api.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ClinicDto(
        String id,
        String name,
        String description,
        String phone,
        String licenseNumber,
        AddressDto address,
        String logoUrl,
        List<String> workingHours,
        List<VetShortDto> vets,
        List<String> services,
        double rating,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public record AddressDto(
            String city,
            String street,
            String building,
            String postalCode
    ) {}

    public record VetShortDto(
            String id,
            String firstName,
            String lastName,
            String specialization,
            String avatarUrl
    ) {}
}
