package com.vet.profile_service.api.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record PetUpdateDto(
        @NotBlank String name,
        PetType type,
        String breed,
        LocalDate birthDate,
        String chipNumber
) {}
