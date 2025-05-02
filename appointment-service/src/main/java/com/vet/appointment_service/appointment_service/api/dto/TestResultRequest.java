package com.vet.appointment_service.appointment_service.api.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TestResultRequest {
    private String testType;
    private double value;
    private LocalDate date;
    private String breed;
}