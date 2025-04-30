package com.vet.profile_service.core.entity;

import com.vet.profile_service.api.dto.UserRole;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Document(collection = "pet_owners")
public class Profile {
    @Id
    private String id;

    @NotBlank
    private String userId;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    private String phone;
    private String avatarUrl;
    private String city;
    private String street;
    private String building;
    private String postalCode;
    private String email;

    @DBRef
    private Set<Pet> pets = new HashSet<>();

    @CreatedDate
    private LocalDateTime createdAt;

}
