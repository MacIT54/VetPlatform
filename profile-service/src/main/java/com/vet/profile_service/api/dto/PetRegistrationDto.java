package com.vet.profile_service.api.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record PetRegistrationDto(
        @NotBlank String name,
        @NotNull PetType type,
        @NotBlank String breed,
        @NotNull LocalDate birthDate,
        @Nullable String chipNumber,
        @NotNull Long ownerId
) {}
