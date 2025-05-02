package com.vet.appointment_service.appointment_service.api.client;

import com.vet.appointment_service.appointment_service.api.dto.LabResultDto;
import com.vet.appointment_service.appointment_service.api.dto.LabTestRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "lab-service", url = "${laboratory.service.url}")
public interface LabServiceClient {

    @PostMapping("/store_result")
    ResponseEntity<LabResultDto> storeTestResult(@RequestBody LabTestRequest request);

    @GetMapping(value = "/generate_report/{petId}", produces = MediaType.APPLICATION_PDF_VALUE)
    ResponseEntity<byte[]> generateReport(@PathVariable String petId);
}