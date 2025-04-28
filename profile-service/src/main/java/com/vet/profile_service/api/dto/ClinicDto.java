package com.vet.profile_service.api.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;


public record ClinicDto(
        String id,
        String name,
        String description,
        String phone,
        String email,
        String city,
        String street,
        String building,
        String postalCode,
        Set<String> services,
        String logoUrl,
        List<String> workingHours,
        List<VetShortDto> vets
) {}