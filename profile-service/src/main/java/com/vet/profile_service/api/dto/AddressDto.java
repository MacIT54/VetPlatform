package com.vet.profile_service.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;

@Builder
public record AddressDto(
        @NotBlank(message = "Город обязателен")
        String city,

        @NotBlank(message = "Улица обязательна")
        String street,

        @NotBlank(message = "Номер дома обязателен")
        String building,

        @NotBlank(message = "Почтовый индекс обязателен")
        @Pattern(regexp = "^[0-9]{6}$", message = "Почтовый индекс должен состоять из 6 цифр")
        String postalCode
) {}
