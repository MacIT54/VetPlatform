package com.vet.profile_service.core.entity;

import com.vet.profile_service.api.dto.PetType;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "pets")
@Data
public class Pet {
    @Id
    private String id;

    @NotBlank
    private String name;

    @NotBlank
    private PetType type;

    private String breed;
    private LocalDate birthDate;
    private String chipNumber;

    @DBRef
    private Profile owner;

    @DBRef
    private List<MedicalRecord> medicalRecords = new ArrayList<>();
}
