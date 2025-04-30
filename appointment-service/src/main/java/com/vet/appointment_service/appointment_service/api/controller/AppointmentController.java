package com.vet.appointment_service.appointment_service.api.controller;

import com.vet.appointment_service.appointment_service.api.dto.AppointmentDto;
import com.vet.appointment_service.appointment_service.api.dto.BookAppointmentRequest;
import com.vet.appointment_service.appointment_service.api.dto.TimeSlotDto;
import com.vet.appointment_service.appointment_service.core.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
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
public class AppointmentController {

    private final AppointmentService appointmentService;

    @GetMapping("/available/{vetId}")
    public List<TimeSlotDto> getAvailableSlots(
            @PathVariable String vetId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return appointmentService.getAvailableSlots(vetId, date);
    }

    @PostMapping("/book")
    public AppointmentDto bookAppointment(
            @RequestHeader("Authorization") String token,
            @RequestBody BookAppointmentRequest request) {
        return appointmentService.bookAppointment(request);
    }

}
