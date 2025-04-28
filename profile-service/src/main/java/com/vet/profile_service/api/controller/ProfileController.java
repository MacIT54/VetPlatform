package com.vet.profile_service.api.controller;

import com.vet.profile_service.api.dto.UserProfileDto;
import com.vet.profile_service.api.dto.UserProfileUpdateDto;
import com.vet.profile_service.api.dto.VetDto;
import com.vet.profile_service.core.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @Operation(summary = "Получить мой профиль", description = "Возвращает данные текущего пользователя")
    @ApiResponse(responseCode = "200", description = "Успешный запрос")
    @GetMapping("/me")
    public UserProfileDto getMyProfile() {
        return profileService.getCurrentUserProfile();
    }

//    @Operation(summary = "Обновить профиль", description = "Изменяет данные профиля")
//    @ApiResponse(responseCode = "200", description = "Профиль обновлен")
//    @PutMapping("/me")
//    public UserProfileDto updateMyProfile(
//            @RequestBody @Schema(description = "Новые данные профиля", required = true) UserProfileUpdateDto updateDto) {
//        return profileService.updateCurrentUserProfile(updateDto);
//    }

    @Operation(
            summary = "Поиск ветеринаров",
            description = "Возвращает список ветеринаров с фильтрацией",
            parameters = {
                    @Parameter(name = "specialization", description = "Специализация", example = "Хирургия"),
                    @Parameter(name = "city", description = "Город", example = "Москва")
            }
    )
    @ApiResponse(responseCode = "200", description = "Успешный запрос")
    @GetMapping("/vets")
    public List<VetDto> searchVets(
            @RequestParam(required = false) String specialization,
            @RequestParam(required = false) String city) {
        return profileService.searchVets(specialization, city);
    }
}