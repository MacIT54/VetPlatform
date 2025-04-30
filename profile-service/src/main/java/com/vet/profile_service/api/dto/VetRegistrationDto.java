package com.vet.profile_service.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.util.Set;

@Schema(description = "DTO для регистрации ветеринара")
public record VetRegistrationDto(
        @Schema(description = "Имя ветеринара", example = "Иван", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank String firstName,

        @Schema(description = "Фамилия ветеринара", example = "Петров", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank String lastName,

        @Schema(description = "Специализация", example = "Хирург", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank String specialization,

        @Schema(description = "Квалификация", example = "Высшая категория")
        String qualification,

        @Schema(description = "Биография", example = "Опыт работы 10 лет, специализация на ортопедии")
        String bio,

        @Schema(description = "Предоставляемые услуги",
                example = "[\"Консультация\", \"Операция\", \"УЗИ\"]")
        Set<String> services,

        @Schema(description = "Адрес (только для независимых ветеринаров)")
        AddressDto addressDto
) {}