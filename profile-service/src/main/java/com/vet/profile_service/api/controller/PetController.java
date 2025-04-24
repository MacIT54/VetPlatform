package com.vet.profile_service.api.controller;

import com.vet.profile_service.api.dto.MedicalRecordDto;
import com.vet.profile_service.api.dto.PetRegistrationDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profiles/pets")
public class PetController {

    @PostMapping
    public ResponseEntity<?> addPet(@RequestBody PetRegistrationDto petDto) {
        return null;
    }

    @GetMapping("/my")
    public ResponseEntity<?> getMyPets() {
        return null;
    }

    @PostMapping("/{petId}/medical-records")
    public ResponseEntity<?> addMedicalRecord(
            @PathVariable Long petId,
            @RequestBody MedicalRecordDto recordDto) {
        return null;
    }

    @GetMapping("/{petId}/medical-records")
    public ResponseEntity<?> getMedicalRecords(@PathVariable Long petId) {
        return null;
    }
}