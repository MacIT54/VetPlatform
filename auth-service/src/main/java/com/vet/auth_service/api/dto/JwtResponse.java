package com.vet.auth_service.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class JwtResponse {
    private String token;
    private final String type = "Bearer";

    public JwtResponse(String token) {
        this.token = token;
    }
}

