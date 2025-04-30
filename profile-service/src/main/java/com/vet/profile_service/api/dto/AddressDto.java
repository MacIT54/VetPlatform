package com.vet.profile_service.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;

public record AddressDto(
        @NotBlank(message = "Город обязателен")
        String city,

        @NotBlank(message = "Улица обязательна")
        String street,

        @NotBlank(message = "Номер дома обязателен")
        String building
) {}
