package com.vet.profile_service.core.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "clinics_vets")
public class ClinicVet {
    @Id
    private String id;
    private String clinicId;
    private String vetId;
}