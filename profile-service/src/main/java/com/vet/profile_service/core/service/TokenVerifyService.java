package com.vet.profile_service.core.service;

import com.vet.profile_service.api.client.AuthServiceClient;
import com.vet.profile_service.api.client.entity.TokenVerifyRequest;
import com.vet.profile_service.api.client.entity.TokenVerifyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class TokenVerifyService {

    private final AuthServiceClient authServiceClient;

    public void verifyToken(String token) {
        TokenVerifyResponse response = authServiceClient.verifyToken(
                new TokenVerifyRequest(extractToken(token))
        );

        if (response.isEnabled()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User account is disabled");
        }
    }

    public void verifyTokenAndCheckAdminRole(String token) {
        TokenVerifyResponse response = authServiceClient.verifyToken(
                new TokenVerifyRequest(extractToken(token))
        );

        if (!"ADMIN".equals(response.role())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Admin role required");
        }
    }

    public void verifyTokenAndCheckAdminOrVetRole(String token) {
        TokenVerifyResponse response = authServiceClient.verifyToken(
                new TokenVerifyRequest(extractToken(token))
        );

        if (!"ADMIN".equals(response.role()) && !"VET".equals(response.role())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Admin or Vet role required");
        }
    }

    public TokenVerifyResponse tokenResponse(String token) {
        return authServiceClient.verifyToken(
                new TokenVerifyRequest(extractToken(token))
        );
    }

    public String extractToken(String authHeader) {
        if (authHeader != null) {
            return authHeader;
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid authorization header");
    }
}
