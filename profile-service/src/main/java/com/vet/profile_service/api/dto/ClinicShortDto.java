package com.vet.profile_service.api.dto;

import java.util.List;

public record ClinicShortDto(
        String id,
        String name,
        String city,
        String logoUrl,
        double rating,
        List<String> mainServices
) {}