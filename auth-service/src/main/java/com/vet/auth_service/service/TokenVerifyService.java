package com.vet.auth_service.service;

import com.vet.auth_service.api.dto.TokenVerifyRequest;
import com.vet.auth_service.api.dto.TokenVerifyResponse;
import com.vet.auth_service.api.exception.InvalidTokenException;
import com.vet.auth_service.model.TokenSession;
import com.vet.auth_service.repository.ITokenSessionRepository;
import com.vet.auth_service.security.jwt.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenVerifyService {

    private final JwtTokenProvider provider;
    private final ITokenSessionRepository tokenSessionRepository;

    public TokenVerifyResponse verifyToken(TokenVerifyRequest request) {
        if (!provider.validateToken(request.token())) {
            throw new InvalidTokenException("Invalid or expired token");
        }

        TokenSession session = tokenSessionRepository.findByToken(request.token())
                .orElseThrow(() -> new InvalidTokenException("Session not found for token"));

        if (session.isRevoked()) {
            throw new InvalidTokenException("Token has been revoked");
        }

        Claims claims = provider.getClaims(request.token());

        String username = claims.getSubject();
        String id = claims.get("id", String.class);
        String role = claims.get("role", String.class);
        String name = claims.get("name", String.class);
        String surname = claims.get("surname", String.class);
        String email = claims.get("email", String.class);
        boolean isEnabled = claims.get("enabled", Boolean.class);

        return new TokenVerifyResponse(id, username, role, name, surname, email, isEnabled);
    }
}
