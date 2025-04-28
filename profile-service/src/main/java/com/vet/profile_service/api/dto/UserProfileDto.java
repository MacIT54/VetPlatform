package com.vet.profile_service.api.dto;

import jakarta.annotation.Nullable;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record UserProfileDto(
        String id,
        String userId,
        String firstName,
        String lastName,
        String phone,
        String avatarUrl,
        AddressDto addressDto,
        List<PetShortDto> pets
) {
}
