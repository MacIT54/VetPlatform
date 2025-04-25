package com.vet.profile_service.api.controller;

import com.vet.profile_service.api.dto.UserProfileDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/profiles")
@Tag(name = "Admin API", description = "Управление пользователями и клиниками (только для админов)")
public class AdminController {

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
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return null;
    }

    @Operation(
            summary = "Удалить клинику",
            description = "Удаляет клинику по ID (только для админов)"
    )
    @ApiResponse(responseCode = "204", description = "Клиника удалена")
    @ApiResponse(responseCode = "404", description = "Клиника не найдена")
    @DeleteMapping("/clinics/{clinicId}")
    public void deleteClinic(@PathVariable Long clinicId) {
    }
}