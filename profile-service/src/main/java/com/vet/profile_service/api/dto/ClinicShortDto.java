package com.vet.profile_service.api.dto;

import lombok.Builder;

import java.util.List;

public record ClinicShortDto(
        String id,
        String name,
        String logoUrl
) {}