package com.vet.auth_service.api.client;

import com.vet.auth_service.api.dto.AppointmentDto;
import com.vet.auth_service.api.dto.UserRegistrationDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "profile-service", url = "${profile.service.url}")
public interface ProfileServiceClient {
    @GetMapping("/api/profiles/users/create")
    List<AppointmentDto> createProfile(UserRegistrationDto userDto);
}
