package com.vet.profile_service.core.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "medical_records")
@Data
public class MedicalRecord {
    @Id
    private String id;

    @NotBlank
    private String diagnosis;

    private String treatment;
    private String notes;
    private LocalDate date;

    @DBRef
    private Vet vet;

    @DBRef
    private Pet pet;
}