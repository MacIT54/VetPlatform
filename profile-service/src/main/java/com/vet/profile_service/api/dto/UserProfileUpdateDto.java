package com.vet.profile_service.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

public record UserProfileUpdateDto(
        @NotBlank String firstName,
        @NotBlank String lastName,
        String phone,
        AddressDto address,
        String avatarUrl
) {}
