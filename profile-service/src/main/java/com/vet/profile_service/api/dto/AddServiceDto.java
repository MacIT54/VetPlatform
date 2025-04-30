package com.vet.profile_service.api.dto;

import jakarta.validation.constraints.NotBlank;

public record AddServiceDto(
        @NotBlank String serviceName
) {}
