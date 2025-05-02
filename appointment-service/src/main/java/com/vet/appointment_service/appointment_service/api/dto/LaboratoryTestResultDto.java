package com.vet.appointment_service.appointment_service.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LaboratoryTestResultDto {
    private String petId;
    private String testType;
    private Double value;
    private String date;
    private String breed;
}