package com.vet.profile_service.api.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public record VetRegistrationDto(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank String specialization,
        String qualification,
        String bio,
        Set<String> services,
        AddressDto addressDto
) {}
