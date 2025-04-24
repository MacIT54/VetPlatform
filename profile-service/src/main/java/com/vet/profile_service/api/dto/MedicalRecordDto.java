package com.vet.profile_service.api.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record MedicalRecordDto(
        @NotBlank String diagnosis,
        @NotBlank String treatment,
        @NotNull LocalDate date,
        @Nullable String notes,
        @NotNull Long vetId,
        @NotNull Long petId
) {}
