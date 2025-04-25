package com.vet.profile_service.api.controller;

import com.vet.profile_service.api.dto.ClinicDto;
import com.vet.profile_service.api.dto.ClinicShortDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/profiles/clinics")
@Tag(name = "Clinic API", description = "Управление клиниками и ветеринарами")
public class ClinicController {

    @Operation(
            summary = "Добавить ветеринара в клинику",
            description = "Привязывает ветеринара к клинике (доступно для админов и ветеринаров)"
    )
    @ApiResponse(responseCode = "200", description = "Ветеринар добавлен", content = @Content(schema = @Schema(implementation = ClinicDto.class)))
    @PostMapping("/{clinicId}/vets/{vetId}")
    public ClinicDto addVetToClinic(
            @Parameter(description = "ID клиники", required = true) @PathVariable Long clinicId,
            @Parameter(description = "ID ветеринара", required = true) @PathVariable Long vetId) {
        return null;
    }

    @Operation(summary = "Получить клинику", description = "Возвращает данные текущей клиники")
    @ApiResponse(responseCode = "200", description = "Успешный запрос")
    @GetMapping
    public ClinicShortDto getClinic() {
        return null;
    }

    @Operation(summary = "Получить все клиники", description = "Возвращает список всех клиник")
    @ApiResponse(responseCode = "200", description = "Успешный запрос")
    @GetMapping("/all")
    public List<ClinicShortDto> getAllClinics() {
        return null;
    }
}