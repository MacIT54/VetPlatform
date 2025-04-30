package com.vet.profile_service.api.client;

import com.vet.profile_service.api.dto.AppointmentDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "appointment-service", url = "${appointment.service.url}")
public interface AppointmentServiceClient {
    @GetMapping("/api/appointments/vet/{vetId}")
    List<AppointmentDto> getVetAppointments(@PathVariable String vetId);
}