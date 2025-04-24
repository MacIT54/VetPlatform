package com.vet.profile_service.api.controller;

import com.vet.profile_service.api.dto.UserProfileUpdateDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profiles/users")
public class ProfileController {

    @GetMapping("/me")
    public ResponseEntity<?> getMyProfile() {
        return null;
    }

    @PutMapping("/me")
    public ResponseEntity<?> updateMyProfile(@RequestBody UserProfileUpdateDto updateDto) {
        return null;
    }

    @GetMapping("/vets")
    public ResponseEntity<?> searchVets(
            @RequestParam(required = false) String specialization,
            @RequestParam(required = false) String city) {
        return null;
    }
}