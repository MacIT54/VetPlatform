package com.vet.auth_service.api.controller;

import com.vet.auth_service.api.dto.*;
import com.vet.auth_service.repository.ITokenSessionRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.vet.auth_service.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final ITokenSessionRepository tokenSessionRepository;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest request) {
        SignupResponse response = authService.signup(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public JwtResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/change-password")
    public ChangePasswordResponse changePassword(@RequestBody ChangePasswordRequest request) {
        authService.changePassword(request);
        return new ChangePasswordResponse("Change Password Successful");
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String bearerToken) {
        String token = bearerToken.substring(7);

        tokenSessionRepository.findByToken(token).ifPresent(session -> {
            session.setRevoked(true);
            tokenSessionRepository.save(session);
        });

        return ResponseEntity.ok().build();
    }
}
