package com.vet.profile_service.core.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Document(collection = "clinics")
@Data
public class Clinic {
    @Id
    private String id;

    @NotBlank
    private String name;

    private String description;
    private String phone;
    private String email;

    @DBRef
    private Set<Vet> vets = new HashSet<>();

    private String city;
    private String street;
    private String building;
    private String postalCode;

    private Set<String> services;
    private String logoUrl;
    private List<String> workingHours;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private boolean isDeleted;
}
