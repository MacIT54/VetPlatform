package com.vet.profile_service.api.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

public record PetRegistrationDto(
        @NotBlank String name,
        @NotNull PetType type,
        String breed,
        LocalDate birthDate,
        String chipNumber
) {}
