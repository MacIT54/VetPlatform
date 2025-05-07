package com.vet.profile_service.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Set;

@Schema(description = "DTO клиники")
public record ClinicUpdateDto(
        @Schema(description = "ID клиники", example = "65a1bc4e3f8d9c1f2e7f8a9b")
        String id,

        @Schema(description = "Название клиники", example = "ВетКлиника №1")
        String name,

        @Schema(description = "Описание", example = "Современная ветеринарная клиника с полным спектром услуг")
        String description,

        @Schema(description = "Телефон", example = "+79161234567")
        String phone,

        @Schema(description = "Email", example = "clinic@example.com")
        String email,

        @Schema(description = "Город", example = "Москва")
        String city,

        @Schema(description = "Улица", example = "Ленина")
        String street,

        @Schema(description = "Дом", example = "10")
        String building,

        @Schema(description = "Почтовый индекс", example = "123456")
        String postalCode,

        @Schema(description = "Услуги клиники",
                example = "[\"Стационар\", \"Лаборатория\", \"Рентген\"]")
        Set<String> services,

        @Schema(description = "URL логотипа", example = "https://example.com/logo.png")
        String logoUrl,

        @Schema(description = "График работы",
                example = "[\"Пн-Пт: 9:00-18:00\", \"Сб: 10:00-15:00\"]")
        List<String> workingHours
) {}