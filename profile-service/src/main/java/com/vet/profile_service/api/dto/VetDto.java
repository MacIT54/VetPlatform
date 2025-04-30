package com.vet.profile_service.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import lombok.Builder;
import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails;
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators;

import java.util.List;
import java.util.Set;


@Schema(description = "DTO ветеринара")
public record VetDto(
        @Schema(description = "ID ветеринара", example = "65a1bc4e3f8d9c1f2e7f8a9c")
        String id,

        @Schema(description = "Имя", example = "Иван")
        String firstName,

        @Schema(description = "Фамилия", example = "Петров")
        String lastName,

        @Schema(description = "Специализация", example = "Хирург")
        String specialization,

        @Schema(description = "Квалификация", example = "Высшая категория")
        String qualification,

        @Schema(description = "Биография", example = "Специалист по ортопедическим операциям")
        String bio,

        @Schema(description = "Email", example = "vet@example.com")
        String email,

        @Schema(description = "URL аватара", example = "https://example.com/avatar.jpg")
        String avatarUrl,

        @Schema(description = "Клиника (null если независимый)")
        ClinicShortDto clinic,

        @Schema(description = "Услуги", example = "[\"Операции\", \"Консультации\"]")
        Set<String> services,

        @Schema(description = "Адрес (для независимых ветеринаров)")
        AddressDto addressDto
) {}