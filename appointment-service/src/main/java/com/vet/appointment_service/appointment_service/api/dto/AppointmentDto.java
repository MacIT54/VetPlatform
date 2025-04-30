package com.vet.appointment_service.appointment_service.api.dto;

import com.vet.appointment_service.appointment_service.core.entity.AppointmentStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AppointmentDto(
        Long id,
        String clinicId,
        String vetId,
        String petId,
        LocalDateTime startTime,
        LocalDateTime endTime,
        AppointmentStatus status,
        String notes
) {}