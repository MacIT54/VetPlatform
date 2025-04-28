package com.vet.profile_service.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record MedicalRecordCreationDto(
        @NotBlank String diagnosis,
        String treatment,
        String notes,
        @NotNull LocalDate date,
        @NotBlank String vetId
) {}