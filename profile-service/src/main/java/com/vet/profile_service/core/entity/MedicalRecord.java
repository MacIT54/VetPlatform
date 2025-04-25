package com.vet.profile_service.core.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "medical_records")
public class MedicalRecord {
    @Id
    private String id;
    private String diagnosis;
    private String treatment;
    private LocalDate date;
    private String notes;
    private String petId;
    private String vetId;
}