package com.vet.appointment_service.appointment_service.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LabTestRequest {
    @JsonProperty("pet_id")
    private String petId;

    @JsonProperty("test_type")
    private String testType;
    private double value;
    private String date;
    private String breed;
}