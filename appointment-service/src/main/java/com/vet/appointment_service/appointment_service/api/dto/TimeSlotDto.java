package com.vet.appointment_service.appointment_service.api.dto;

import java.time.LocalDateTime;

public record TimeSlotDto(
        String vetId,
        LocalDateTime startTime,
        LocalDateTime endTime,
        boolean available
) {}
