package com.vet.profile_service.api.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
public record AddressDto(
        String city,
        String street,
        String building
) {}
