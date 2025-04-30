package com.vet.profile_service.api.dto;

import java.util.Set;

public record VetUpdateDto(
        String firstName,
        String lastName,
        String specialization,
        String qualification,
        String bio,
        Set<String> services,
        String clinicId
) {}