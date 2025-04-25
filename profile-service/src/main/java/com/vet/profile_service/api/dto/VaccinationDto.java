package com.vet.profile_service.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record VaccinationDto(
        @NotBlank String vaccineName,
        @NotNull LocalDate vaccinationDate,
        @NotNull LocalDate nextVaccinationDate,
        @NotNull Long petId
) {}
