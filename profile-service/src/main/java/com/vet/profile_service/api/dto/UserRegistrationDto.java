package com.vet.profile_service.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UserRegistrationDto(
        @NotBlank String userId,
        @NotBlank String firstName,
        @NotBlank String lastName,
        String email
) {
}
