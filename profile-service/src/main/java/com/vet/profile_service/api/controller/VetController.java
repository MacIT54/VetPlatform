package com.vet.profile_service.api.controller;

import com.vet.profile_service.api.dto.AddServiceDto;
import com.vet.profile_service.api.dto.AppointmentDto;
import com.vet.profile_service.api.dto.VetDto;
import com.vet.profile_service.api.dto.VetRegistrationDto;
import com.vet.profile_service.api.dto.VetUpdateDto;
import com.vet.profile_service.core.service.VetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/vets")
@Tag(name = "Vet API", description = "Управление ветеринарами")
@RequiredArgsConstructor
public class VetController {

    private final VetService vetService;

    @Operation(summary = "Получить всех ветеринаров")
    @ApiResponse(responseCode = "200", description = "Список ветеринаров")
    @GetMapping
    public List<VetDto> getAllVets(
            @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "Токен авторизации", required = true)
            @RequestHeader("Authorization") String token) {
        return vetService.getAllVets(token);
    }

    @Operation(summary = "Получить ветеринаров по специализации")
    @ApiResponse(responseCode = "200", description = "Список ветеринаров")
    @GetMapping("/by-specialization")
    public List<VetDto> getVetsBySpecialization(
            @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "Токен авторизации", required = true)
            @RequestHeader("Authorization") String token,
            @RequestParam String specialization) {
        return vetService.getVetsBySpecialization(specialization, token);
    }

    @Operation(summary = "Получить независимых ветеринаров")
    @ApiResponse(responseCode = "200", description = "Список независимых ветеринаров")
    @GetMapping("/independent")
    public List<VetDto> getIndependentVets(
            @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "Токен авторизации", required = true)
            @RequestHeader("Authorization") String token) {
        return vetService.getIndependentVets(token);
    }

    @Operation(summary = "Получить ветеринаров клиники")
    @ApiResponse(responseCode = "200", description = "Список ветеринаров клиники")
    @GetMapping("/by-clinic/{clinicId}")
    public List<VetDto> getVetsByClinic(
            @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "Токен авторизации", required = true)
            @RequestHeader("Authorization") String token,
            @PathVariable String clinicId) {
        return vetService.getVetsByClinic(clinicId, token);
    }

    @Operation(summary = "Добавить нового ветеринара")
    @ApiResponse(responseCode = "201", description = "Ветеринар создан")
    @PostMapping
    public VetDto createVet(
            @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "Токен авторизации", required = true)
            @RequestHeader("Authorization") String token,
            @RequestBody VetRegistrationDto registrationDto
    ) {
        return vetService.createVet(registrationDto, token);
    }

    @Operation(summary = "Обновить данные ветеринара")
    @ApiResponse(responseCode = "200", description = "Данные обновлены")
    @PutMapping("/{vetId}")
    public VetDto updateVet(
            @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "Токен авторизации", required = true)
            @RequestHeader("Authorization") String token,
            @PathVariable String vetId,
            @RequestBody VetUpdateDto updateDto) {
        return vetService.updateVet(vetId, updateDto, token);
    }

    @Operation(summary = "Получить записи на приём")
    @ApiResponse(responseCode = "200", description = "Список записей")
    @GetMapping("/{vetId}/appointments")
    public List<AppointmentDto> getVetAppointments(
            @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "Токен авторизации", required = true)
            @RequestHeader("Authorization") String token,
            @PathVariable String vetId) {
        return vetService.getVetAppointments(vetId, token);
    }

    @Operation(summary = "Добавить услугу ветеринару")
    @ApiResponse(responseCode = "200", description = "Услуга добавлена")
    @PostMapping("/{vetId}/services")
    public VetDto addServiceToVet(
            @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "Токен авторизации", required = true)
            @RequestHeader("Authorization") String token,
            @PathVariable String vetId,
            @RequestBody AddServiceDto serviceDto) {
        return vetService.addServiceToVet(vetId, serviceDto.serviceName(), token);
    }
}
