package com.vet.profile_service.api.dto;

import java.time.LocalDate;
import java.util.List;

public record PetDto(
        String id,
        String name,
        PetType type,
        String breed,
        LocalDate birthDate,
        String chipNumber,
        List<MedicalRecordDto> medicalRecords
) {}