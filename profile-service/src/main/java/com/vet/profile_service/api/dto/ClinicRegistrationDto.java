package com.vet.profile_service.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

public record ClinicRegistrationDto(
        @NotBlank(message = "Название клиники обязательно")
        @Size(max = 100, message = "Название клиники не должно превышать 100 символов")
        String name,

        @NotBlank(message = "Описание клиники обязательно")
        @Size(max = 1000, message = "Описание клиники не должно превышать 1000 символов")
        String description,

        @NotBlank(message = "Телефон обязателен")
        @Pattern(regexp = "^\\+?[0-9\\s-]{10,20}$", message = "Некорректный формат телефона")
        String phone,

        @NotBlank(message = "Email обязателен")
        @Email(message = "Некорректный формат email")
        String email,

        @NotNull(message = "Адрес обязателен")
        AddressDto address,

        @NotBlank(message = "Номер лицензии обязателен")
        String licenseNumber,

        List<String> workingHours
) {}