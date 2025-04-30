package com.vet.appointment_service.appointment_service.api.dto;

import jakarta.validation.constraints.NotBlank;

public record AddressDto(
        @NotBlank(message = "Город обязателен")
        String city,

        @NotBlank(message = "Улица обязательна")
        String street,

        @NotBlank(message = "Номер дома обязателен")
        String building
) {}
