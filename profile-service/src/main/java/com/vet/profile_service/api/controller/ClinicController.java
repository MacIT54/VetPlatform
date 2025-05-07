package com.vet.profile_service.api.controller;

import com.vet.profile_service.api.dto.ClinicDto;
import com.vet.profile_service.api.dto.ClinicRegistrationDto;
import com.vet.profile_service.api.dto.ClinicShortDto;
import com.vet.profile_service.api.dto.ClinicUpdateDto;
import com.vet.profile_service.api.dto.VetDto;
import com.vet.profile_service.api.dto.VetRegistrationDto;
import com.vet.profile_service.core.repository.ClinicRepository;
import com.vet.profile_service.core.service.ClinicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/profiles/clinics")
@Tag(name = "Clinic API", description = "Управление клиниками и ветеринарами")
@RequiredArgsConstructor
public class ClinicController {

    private final ClinicService clinicService;

    @Operation(
            summary = "Создать клинику",
            description = "Создаёт клинику"
    )
    @ApiResponse(responseCode = "200", description = "Клиника создана", content = @Content(schema = @Schema(implementation = ClinicDto.class)))
    @PostMapping
    public ClinicDto createClinic(
            @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "Токен авторизации", required = true)
            @RequestHeader("Authorization") String token,
            @RequestBody ClinicRegistrationDto dto) {
        return clinicService.createClinic(dto, token);
    }

    @Operation(
            summary = "Обновить клинику",
            description = "Обновляет данные клиники"
    )
    @ApiResponse(responseCode = "200", description = "Клиника обновлена", content = @Content(schema = @Schema(implementation = ClinicDto.class)))
    @PutMapping("/{clinicId}")
    public ClinicDto updateClinic(
            @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "Токен авторизации", required = true)
            @RequestHeader("Authorization") String token,
            @Parameter(description = "ID клиники", required = true) @PathVariable String clinicId,
            @RequestBody ClinicUpdateDto dto) {
        return clinicService.updateClinic(dto, token, clinicId);
    }

    @Operation(
            summary = "Добавить ветеринара в клинику",
            description = "Привязывает ветеринара к клинике (доступно для админов и ветеринаров)"
    )
    @ApiResponse(responseCode = "200", description = "Ветеринар добавлен", content = @Content(schema = @Schema(implementation = ClinicDto.class)))
    @PostMapping("/{clinicId}/vets/{vetId}")
    public ClinicDto addVetToClinic(
            @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "Токен авторизации", required = true)
            @RequestHeader("Authorization") String token,
            @Parameter(description = "ID клиники", required = true) @PathVariable String clinicId,
            @Parameter(description = "ID ветеринара", required = true) @PathVariable String vetId) {
        return clinicService.addVetToClinic(clinicId, vetId, token);
    }

    @Operation(
            summary = "Удалить ветеринара из клиники",
            description = "Отвязывает ветеринара от клиники (доступно для админов и ветеринаров)"
    )
    @ApiResponse(responseCode = "200", description = "Ветеринар удален", content = @Content(schema = @Schema(implementation = ClinicDto.class)))
    @DeleteMapping("/{clinicId}/vets/{vetId}")
    public ClinicDto deleteVetFromClinic(
            @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "Токен авторизации", required = true)
            @RequestHeader("Authorization") String token,
            @Parameter(description = "ID клиники", required = true) @PathVariable String clinicId,
            @Parameter(description = "ID ветеринара", required = true) @PathVariable String vetId) {
        return clinicService.deleteVetFromClinic(clinicId, vetId, token);
    }

    @Operation(summary = "Получить клинику", description = "Возвращает данные текущей клиники")
    @ApiResponse(responseCode = "200", description = "Успешный запрос")
    @GetMapping("/{clinicId}")
    public ClinicDto getClinic(
            @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "Токен авторизации", required = true)
            @RequestHeader("Authorization") String token,
            @Parameter(description = "ID клиники", required = true) @PathVariable String clinicId) {
        return clinicService.getClinic(clinicId, token);
    }

    @Operation(summary = "Получить все клиники", description = "Возвращает список всех клиник")
    @ApiResponse(responseCode = "200", description = "Успешный запрос")
    @GetMapping("/all")
    public List<ClinicShortDto> getAllClinics(
            @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "Токен авторизации", required = true)
            @RequestHeader("Authorization") String token
    ) {
        return clinicService.getAllClinics(token);
    }

    @Operation(
            summary = "Удалить клинику",
            description = "Удаляет клинику по ID (только для админов)"
    )
    @ApiResponse(responseCode = "204", description = "Клиника удалена")
    @ApiResponse(responseCode = "404", description = "Клиника не найдена")
    @DeleteMapping("/clinics/{clinicId}")
    public void deleteClinic(
            @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "Токен авторизации", required = true)
            @RequestHeader("Authorization") String token,
            @PathVariable String clinicId) {
        clinicService.deleteClinic(clinicId);
    }
}