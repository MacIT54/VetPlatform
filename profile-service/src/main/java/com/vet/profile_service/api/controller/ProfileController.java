package com.vet.profile_service.api.controller;

import com.vet.profile_service.api.dto.PetDto;
import com.vet.profile_service.api.dto.PetRegistrationDto;
import com.vet.profile_service.api.dto.PetUpdateDto;
import com.vet.profile_service.api.dto.UserProfileDto;
import com.vet.profile_service.api.dto.UserProfileUpdateDto;
import com.vet.profile_service.api.dto.UserRegistrationDto;
import com.vet.profile_service.api.dto.VetDto;
import com.vet.profile_service.core.repository.PetRepository;
import com.vet.profile_service.core.service.PetService;
import com.vet.profile_service.core.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/profiles/users")
@Tag(name = "Profile API", description = "Управление профилями пользователей")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @Operation(
            summary = "Создать профиль",
            description = "Создает профиль пользователя",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Профиль создан"),
                    @ApiResponse(responseCode = "400", description = "Неверные данные"),
                    @ApiResponse(responseCode = "403", description = "Доступ запрещен")
            }
    )
    @PostMapping("/create")
    public ResponseEntity<Void> createProfile(
            @Valid @RequestBody UserRegistrationDto userDto) {
        profileService.createProfile(userDto);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Обновить профиль",
            description = "Изменяет данные профиля",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Профиль обновлен"),
                    @ApiResponse(responseCode = "400", description = "Неверные данные"),
                    @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
                    @ApiResponse(responseCode = "404", description = "Профиль не найден")
            }
    )
    @PutMapping("/me")
    public ResponseEntity<UserProfileDto> updateMyProfile(
            @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "Токен авторизации", required = true)
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody UserProfileUpdateDto updateDto) {
        return ResponseEntity.ok(profileService.updateCurrentUserProfile(updateDto, token));
    }

    @Operation(summary = "Получить мой профиль", description = "Возвращает данные текущего пользователя")
    @ApiResponse(responseCode = "200", description = "Успешный запрос")
    @GetMapping("/me")
    public UserProfileDto getMyProfile(
            @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "Токен авторизации", required = true)
            @RequestHeader("Authorization") String token) {
        return profileService.getCurrentUserProfile(token);
    }

    @Operation(
            summary = "Получить всех пользователей",
            description = "Возвращает список пользователей с пагинацией",
            parameters = {
                    @Parameter(name = "page", description = "Номер страницы (начиная с 0)", example = "0"),
                    @Parameter(name = "size", description = "Размер страницы", example = "10")
            }
    )
    @ApiResponse(responseCode = "200", description = "Успешный запрос")
    @GetMapping("/users")
    public List<UserProfileDto> getAllUsers(
            @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "Токен авторизации", required = true)
            @RequestHeader("Authorization") String token,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return profileService.getAllUsers(page, size);
    }
}