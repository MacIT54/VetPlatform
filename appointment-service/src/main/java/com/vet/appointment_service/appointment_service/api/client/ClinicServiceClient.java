package com.vet.appointment_service.appointment_service.api.client;

import com.vet.appointment_service.appointment_service.api.dto.ClinicDto;
import com.vet.appointment_service.appointment_service.api.dto.VetDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "profile-service", url = "${profile.service.url}")
public interface ClinicServiceClient {

    @GetMapping("/api/clinics/{id}")
    ClinicDto getClinicById(@PathVariable Long id);

    @GetMapping("/api/vets/{id}")
    VetDto getVetById(@PathVariable Long id);

}
