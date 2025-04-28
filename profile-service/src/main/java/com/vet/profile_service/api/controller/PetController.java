package com.vet.profile_service.api.controller;

import com.vet.profile_service.api.dto.MedicalRecordDto;
import com.vet.profile_service.api.dto.PetRegistrationDto;
import com.vet.profile_service.api.dto.UserProfileDto;
import com.vet.profile_service.core.service.PetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/profiles/pets")
@Tag(name = "Pet API", description = "Управление питомцами и медкартами")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;

    @Operation(
            summary = "Добавить питомца",
            description = "Создает профиль питомца (доступно для владельцев)"
    )
    @ApiResponse(responseCode = "201", description = "Питомец создан")
    @PostMapping
    public PetRegistrationDto addPet(
            @RequestBody @Schema(description = "Данные питомца", required = true) PetRegistrationDto petDto) {
        return petService.addPet(petDto);
    }

    @Operation(summary = "Получить моих питомцев", description = "Возвращает список питомцев текущего пользователя")
    @ApiResponse(responseCode = "200", description = "Успешный запрос")
    @GetMapping("/my")
    public List<PetRegistrationDto> getMyPets() {
        return petService.getMyPets("1");
    }

    @Operation(
            summary = "Добавить запись в медкарту",
            description = "Добавляет медицинскую запись для питомца (доступно для ветеринаров)"
    )
    @ApiResponse(responseCode = "201", description = "Запись добавлена")
    @PostMapping("/{petId}/medical-records")
    public MedicalRecordDto addMedicalRecord(
            @Parameter(description = "ID питомца", required = true) @PathVariable Long petId,
            @RequestBody @Schema(description = "Данные медкарты", required = true) MedicalRecordDto recordDto) {
        return petService.addMedicalRecord(String.valueOf(petId), recordDto);
    }

    @Operation(summary = "Получить медкарту питомца", description = "Возвращает все записи медкарты")
    @ApiResponse(responseCode = "200", description = "Успешный запрос")
    @GetMapping("/{petId}/medical-records")
    public List<MedicalRecordDto> getMedicalRecords(
            @Parameter(description = "ID питомца", required = true) @PathVariable Long petId) {
        return null;
    }
}