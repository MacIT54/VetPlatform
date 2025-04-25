package com.vet.profile_service.api.dto;

import jakarta.annotation.Nullable;

import java.util.List;

public record ClinicSearchDto(
        @Nullable String city,
        @Nullable List<String> services,
        @Nullable Double minRating,
        int page,
        int size
) {}