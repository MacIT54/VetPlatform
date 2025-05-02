package com.vet.appointment_service.appointment_service.api.controller;

import com.vet.appointment_service.appointment_service.api.dto.AppointmentDto;
import com.vet.appointment_service.appointment_service.api.dto.BookAppointmentRequest;
import com.vet.appointment_service.appointment_service.api.dto.LabResultDto;
import com.vet.appointment_service.appointment_service.api.dto.LaboratoryTestResultDto;
import com.vet.appointment_service.appointment_service.api.dto.TestResultRequest;
import com.vet.appointment_service.appointment_service.api.dto.TimeSlotDto;
import com.vet.appointment_service.appointment_service.core.entity.Appointment;
import com.vet.appointment_service.appointment_service.core.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
@Tag(name = "Управление записями на прием", description = "API для работы с записями к ветеринару")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @Operation(
            summary = "Получить доступные временные слоты",
            description = "Возвращает список доступных временных интервалов для записи к указанному ветеринару на выбранную дату"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Список доступных слотов",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = TimeSlotDto.class)))
    )
    @GetMapping("/available/{vetId}")
    public List<TimeSlotDto> getAvailableSlots(
            @Parameter(description = "ID ветеринара", required = true, example = "vet123")
            @PathVariable String vetId,

            @Parameter(description = "Дата для поиска слотов (формат YYYY-MM-DD)", required = true, example = "2025-05-15")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return appointmentService.getAvailableSlots(vetId, date);
    }

    @Operation(
            summary = "Записаться на прием",
            description = "Создает новую запись на прием к ветеринару"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Созданная запись",
            content = @Content(schema = @Schema(implementation = AppointmentDto.class))
    )
    @PostMapping("/book")
    public AppointmentDto bookAppointment(
            @Parameter(description = "JWT токен авторизации", required = true)
            @RequestHeader("Authorization") String token,

            @Parameter(description = "Данные для создания записи", required = true)
            @RequestBody BookAppointmentRequest request) {
        return appointmentService.bookAppointment(request);
    }

    @Operation(
            summary = "Сохранить результат анализа",
            description = "Сохраняет результаты лабораторных анализов для питомца"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Результаты анализа с интерпретацией",
            content = @Content(schema = @Schema(implementation = LabResultDto.class))
    )
    @PostMapping("/{petId}/test-result")
    public LabResultDto storeTestResult(
            @Parameter(description = "ID питомца", required = true, example = "1")
            @PathVariable String petId,

            @Parameter(description = "Данные лабораторного анализа", required = true)
            @RequestBody TestResultRequest request) {
        return appointmentService.storeTestResult(
                petId,
                request.getTestType(),
                request.getValue(),
                request.getBreed()
        );
    }

    @Operation(
            summary = "Сгенерировать отчет",
            description = "Генерирует PDF отчет с результатами анализов для питомца"
    )
    @ApiResponse(
            responseCode = "200",
            description = "PDF файл с отчетом",
            content = @Content(mediaType = "application/pdf", schema = @Schema(type = "string", format = "binary"))
    )
    @GetMapping(value = "/{petId}/report", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> generateReport(
            @Parameter(description = "ID записи на прием", required = true, example = "1")
            @PathVariable String petId) {
        byte[] report = appointmentService.generateLabReport(petId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=report_" + petId + ".pdf")
                .body(report);
    }
}

