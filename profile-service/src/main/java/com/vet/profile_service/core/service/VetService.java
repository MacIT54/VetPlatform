package com.vet.profile_service.core.service;

import com.vet.profile_service.api.client.AppointmentServiceClient;
import com.vet.profile_service.api.client.AuthServiceClient;
import com.vet.profile_service.api.client.entity.TokenVerifyRequest;
import com.vet.profile_service.api.client.entity.TokenVerifyResponse;
import com.vet.profile_service.api.dto.AddressDto;
import com.vet.profile_service.api.dto.AppointmentDto;
import com.vet.profile_service.api.dto.ClinicShortDto;
import com.vet.profile_service.api.dto.VetDto;
import com.vet.profile_service.api.dto.VetRegistrationDto;
import com.vet.profile_service.api.dto.VetUpdateDto;
import com.vet.profile_service.core.entity.Clinic;
import com.vet.profile_service.core.entity.Vet;
import com.vet.profile_service.core.exception.ErrorCode;
import com.vet.profile_service.core.exception.ServiceException;
import com.vet.profile_service.core.repository.ClinicRepository;
import com.vet.profile_service.core.repository.VetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VetService {

    private final VetRepository vetRepository;
    private final ClinicRepository clinicRepository;
    private final AppointmentServiceClient appointmentServiceClient;
    private final TokenVerifyService tokenVerifyService;
    private final AuthServiceClient authServiceClient;


    public List<VetDto> getAllVets(String token) {
        return vetRepository.findAll().stream()
                .map(this::mapToDto)
                .toList();
    }

    public List<VetDto> getVetsBySpecialization(String specialization, String token) {
        return vetRepository.findBySpecialization(specialization).stream()
                .map(this::mapToDto)
                .toList();
    }

    public List<VetDto> getIndependentVets(String token) {
        return vetRepository.findByClinicIsNull().stream()
                .map(this::mapToDto)
                .toList();
    }

    public List<VetDto> getVetsByClinic(String clinicId, String token) {
        return vetRepository.findByClinicId(clinicId).stream()
                .map(this::mapToDto)
                .toList();
    }

    public VetDto createVet(VetRegistrationDto dto, String token) {
        TokenVerifyResponse authResponse = authServiceClient.verifyToken(
                new TokenVerifyRequest(tokenVerifyService.extractToken(token))
        );

        if (!"ADMIN".equals(authResponse.role())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Только админ может создавать врачей");
        }

        Vet vet = new Vet();
        vet.setFirstName(dto.firstName());
        vet.setLastName(dto.lastName());
        vet.setSpecialization(dto.specialization());
        vet.setQualification(dto.qualification());
        vet.setBio(dto.bio());
        vet.setServices(dto.services());
        vet.setCity(dto.addressDto().city());
        vet.setStreet(dto.addressDto().street());
        vet.setBuilding(dto.addressDto().building());
        vet.setClinic(null);

        Vet savedVet = vetRepository.save(vet);
        return mapToDto(savedVet);
    }

    public VetDto updateVet(String vetId, VetUpdateDto updateDto, String token) {
        Vet vet = vetRepository.findById(vetId)
                .orElseThrow(() -> new ServiceException(ErrorCode.NOT_FOUND, "Vet not found"));

        if (updateDto.firstName() != null) {
            vet.setFirstName(updateDto.firstName());
        }
        if (updateDto.lastName() != null) {
            vet.setLastName(updateDto.lastName());
        }

        Vet updatedVet = vetRepository.save(vet);
        return mapToDto(updatedVet);
    }

    public List<AppointmentDto> getVetAppointments(String vetId, String token) {
        // Заглушка - потом будет вызов appointment-service
        return appointmentServiceClient.getVetAppointments(vetId);
    }

    public VetDto addServiceToVet(String vetId, String serviceName, String token) {
        Vet vet = vetRepository.findById(vetId)
                .orElseThrow(() -> new ServiceException(ErrorCode.NOT_FOUND, "Vet not found"));

        vet.getServices().add(serviceName);
        Vet updatedVet = vetRepository.save(vet);
        return mapToDto(updatedVet);
    }

    private VetDto mapToDto(Vet vet) {
        return new VetDto(
                vet.getId(),
                vet.getFirstName(),
                vet.getLastName(),
                vet.getSpecialization(),
                vet.getQualification(),
                vet.getBio(),
                vet.getAvatarUrl(),
                vet.getEmail(),
                vet.getClinic() != null ?
                        new ClinicShortDto(vet.getClinic().getId(), vet.getClinic().getName(), vet.getClinic().getLogoUrl()) :
                        null,
                vet.getServices(),
                new AddressDto(vet.getCity(), vet.getStreet(), vet.getBuilding())
        );
    }
}
