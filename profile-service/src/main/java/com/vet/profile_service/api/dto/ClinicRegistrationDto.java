package com.vet.profile_service.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ClinicRegistrationDto(
        @NotBlank String name,
        @NotBlank String description,
        @NotBlank String phone,
        @NotNull AddressDto address,
        @NotBlank String licenseNumber,
        List<String> workingHours
) {}
