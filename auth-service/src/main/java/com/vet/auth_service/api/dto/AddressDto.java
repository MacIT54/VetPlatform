package com.vet.auth_service.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record AddressDto(
        @NotBlank(message = "Город обязателен")
        String city,

        @NotBlank(message = "Улица обязательна")
        String street,

        @NotBlank(message = "Номер дома обязателен")
        String building
) {}
