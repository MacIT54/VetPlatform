package com.vet.profile_service.core.entity;

import com.vet.profile_service.api.dto.UserRole;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "users")
public class User {
    @Id
    private String id;
    private Long authId;
    private UserRole role;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String city;
    private String street;
    private String building;
    private String avatarUrl;

    private String specialization;
    private String qualification;
    private String clinicName;
    private String licenseNumber;
    private List<String> workingHours;
}
