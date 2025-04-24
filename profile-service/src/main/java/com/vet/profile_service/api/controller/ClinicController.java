package com.vet.profile_service.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profiles/clinics")
public class ClinicController {

    @PostMapping("/{clinicId}/vets/{vetId}")
    public ResponseEntity<?> addVetToClinic(
            @PathVariable Long clinicId,
            @PathVariable Long vetId) {
        return null;
    }

    @GetMapping
    public ResponseEntity<?> getAllClinics() {
        return null;
    }
}
