package com.vet.appointment_service.appointment_service.api.dto;

import lombok.Data;

@Data
public class LabResultDto {
    private LabAnalysis analysis;
    private String message;

    @Data
    static class LabAnalysis {
        private double[] normRange;
        private double prediction;
        private String status;
    }
}