package com.vet.auth_service.api.controller;

import com.vet.auth_service.api.dto.*;
import com.vet.auth_service.repository.ITokenSessionRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.vet.auth_service.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final ITokenSessionRepository tokenSessionRepository;

    @Operation(
        summary = "Регистрация нового пользователя",
        description = "Создает нового пользователя на основе данных запроса",
        responses = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно зарегистрирован",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = SignupResponse.class))),
            @ApiResponse(responseCode = "400", description = "Ошибка в данных запроса")
        }
    )
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest request) {
        SignupResponse response = authService.signup(request);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Аутентификация пользователя",
        description = "Возвращает JWT токен при успешной аутентификации",
        responses = {
            @ApiResponse(responseCode = "200", description = "Успешный вход",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = JwtResponse.class))),
            @ApiResponse(responseCode = "401", description = "Неверные учетные данные")
        }
    )
    @PostMapping("/login")
    public JwtResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @Operation(
        summary = "Смена пароля пользователя",
        description = "Позволяет сменить пароль текущему пользователю",
        responses = {
            @ApiResponse(responseCode = "200", description = "Пароль успешно изменен",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ChangePasswordResponse.class))),
            @ApiResponse(responseCode = "400", description = "Ошибка смены пароля")
        }
    )
    @PostMapping("/change-password")
    public ChangePasswordResponse changePassword(@RequestBody ChangePasswordRequest request) {
        authService.changePassword(request);
        return new ChangePasswordResponse("Change Password Successful");
    }

    @Operation(
        summary = "Выход из системы",
        description = "Помечает токен как отозванный и завершает сессию пользователя",
        responses = {
            @ApiResponse(responseCode = "200", description = "Выход успешно выполнен"),
            @ApiResponse(responseCode = "401", description = "Неверный токен")
        }
    )
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
