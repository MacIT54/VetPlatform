package com.vet.profile_service.api.dto;

import jakarta.annotation.Nullable;
import lombok.Builder;
import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails;
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators;

import java.util.List;
import java.util.Set;

public record VetDto(
        String id,
        String firstName,
        String lastName,
        String specialization,
        String qualification,
        String bio,
        String avatarUrl,
        ClinicShortDto clinic,
        Set<String> services,
        AddressDto addressDto
) {}
