package com.vet.profile_service.api.dto;

import jakarta.annotation.Nullable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record UserProfileDto(
        String id,
        UserRole role,
        String email,
        String firstName,
        String lastName,
        String phone,
        String avatarUrl,
        AddressDto address,

        LocalDateTime createdAt,
        LocalDateTime updatedAt,

        @Nullable PetOwnerDetailsDto petOwnerDetails,
        @Nullable VetDetailsDto vetDetails,
        @Nullable ClinicDetailsDto clinicDetails
) {

    public record AddressDto(
            String city,
            String street,
            String building
    ) {}

    public record PetOwnerDetailsDto(
            List<PetDto> pets
    ) {}

    public record VetDetailsDto(
            String specialization,
            String qualification,
            @Nullable String clinicId
    ) {}

    public record ClinicDetailsDto(
            String name,
            String licenseNumber,
            List<String> workingHours,
            List<String> vetIds
    ) {}

    public record PetDto(
            String id,
            String name,
            PetType type,
            String breed,
            LocalDate birthDate
    ) {}
}
