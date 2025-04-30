package com.vet.profile_service.api.dto;

import java.time.LocalDateTime;

public record AppointmentDto(
        String id,
        LocalDateTime dateTime,
        String petName,
        String ownerName,
        String status
) {}