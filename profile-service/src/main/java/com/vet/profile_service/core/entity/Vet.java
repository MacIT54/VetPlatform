package com.vet.profile_service.core.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Set;

@Document(collection = "vets")
@Data
public class Vet {
    @Id
    private String id;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String specialization;

    private String qualification;
    private String bio;
    private String email;
    private String avatarUrl;

    @DBRef
    private Clinic clinic;

    private Set<String> services;
    private String city;
    private String street;
    private String building;

    @CreatedDate
    private LocalDateTime createdAt;
}
