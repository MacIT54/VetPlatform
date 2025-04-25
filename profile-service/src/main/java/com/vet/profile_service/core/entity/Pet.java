package com.vet.profile_service.core.entity;

import com.vet.profile_service.api.dto.PetType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "pets")
public class Pet {
    @Id
    private String id;
    private String name;
    private PetType type;
    private String breed;
    private LocalDate birthDate;
    private String chipNumber;
    private String ownerId;
}
