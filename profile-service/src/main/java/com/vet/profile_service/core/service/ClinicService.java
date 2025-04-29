package com.vet.profile_service.core.service;

import com.vet.profile_service.api.client.AuthServiceClient;
import com.vet.profile_service.api.client.entity.TokenVerifyRequest;
import com.vet.profile_service.api.client.entity.TokenVerifyResponse;
import com.vet.profile_service.api.dto.AddressDto;
import com.vet.profile_service.api.dto.ClinicDto;
import com.vet.profile_service.api.dto.ClinicRegistrationDto;
import com.vet.profile_service.api.dto.ClinicShortDto;
import com.vet.profile_service.api.dto.UserRole;
import com.vet.profile_service.api.dto.VetShortDto;
import com.vet.profile_service.core.entity.Clinic;
import com.vet.profile_service.core.entity.Profile;
import com.vet.profile_service.core.entity.Vet;
import com.vet.profile_service.core.exception.ErrorCode;
import com.vet.profile_service.core.exception.ServiceException;
import com.vet.profile_service.core.repository.ClinicRepository;
import com.vet.profile_service.core.repository.UserRepository;
import com.vet.profile_service.core.repository.VetRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.internal.engine.messageinterpolation.parser.Token;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClinicService {
    private final ClinicRepository clinicRepository;
    private final VetRepository vetRepository;
    private final AuthServiceClient authServiceClient;

    public ClinicDto createClinic(ClinicRegistrationDto dto, String token) {
        verifyTokenAndCheckAdminRole(token);
        Clinic clinic = new Clinic();
        clinic.setName(dto.name());
        clinic.setDescription(dto.description());
        clinic.setPhone(dto.phone());
        clinic.setEmail(dto.email());
        clinic.setCity(dto.address().city());
        clinic.setStreet(dto.address().street());
        clinic.setBuilding(dto.address().building());
        clinic.setWorkingHours(dto.workingHours());

        Clinic savedClinic = clinicRepository.save(clinic);
        return mapToClinicDto(savedClinic);
    }

    public ClinicDto addVetToClinic(String clinicId, String vetId, String token) {
        verifyTokenAndCheckAdminOrVetRole(token);
        Clinic clinic = clinicRepository.findByIdAndDeletedFalse(clinicId)
                .orElseThrow(() -> new ServiceException(ErrorCode.NOT_FOUND, "Clinic not found"));

        Vet vet = vetRepository.findById(vetId)
                .orElseThrow(() -> new ServiceException(ErrorCode.NOT_FOUND, "Vet not found"));

        vet.setClinic(clinic);
        clinic.getVets().add(vet);

        vetRepository.save(vet);
        clinicRepository.save(clinic);

        return mapToClinicDto(clinic);
    }

    public ClinicShortDto getClinic(String clinicId, String token) {
        verifyToken(token);
        Clinic clinic = clinicRepository.findByIdAndDeletedFalse(clinicId)
                .orElseThrow(() -> new ServiceException(ErrorCode.NOT_FOUND, "Clinic not found"));
        return new ClinicShortDto(clinic.getId(), clinic.getName(), clinic.getLogoUrl());
    }

    public List<ClinicShortDto> getAllClinics(String token) {
        verifyToken(token);
        return clinicRepository.findAll().stream()
                .map(c -> new ClinicShortDto(c.getId(), c.getName(), c.getLogoUrl()))
                .toList();
    }

    private ClinicDto mapToClinicDto(Clinic clinic) {
        List<VetShortDto> vetDtos = clinic.getVets().stream()
                .map(v -> new VetShortDto(v.getId(), v.getFirstName(), v.getLastName(),
                        v.getSpecialization(), v.getAvatarUrl()))
                .toList();

        return new ClinicDto(
                clinic.getId(),
                clinic.getName(),
                clinic.getDescription(),
                clinic.getPhone(),
                clinic.getEmail(),
                clinic.getCity(),
                clinic.getStreet(),
                clinic.getBuilding(),
                clinic.getPostalCode(),
                clinic.getServices(),
                clinic.getLogoUrl(),
                clinic.getWorkingHours(),
                vetDtos
        );
    }

    private void verifyToken(String token) {
        TokenVerifyResponse response = authServiceClient.verifyToken(
                new TokenVerifyRequest(extractToken(token))
        );

        if (response.isEnabled()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User account is disabled");
        }
    }

    private void verifyTokenAndCheckAdminRole(String token) {
        TokenVerifyResponse response = authServiceClient.verifyToken(
                new TokenVerifyRequest(extractToken(token))
        );

        if (!"ADMIN".equals(response.role())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Admin role required");
        }
    }

    private void verifyTokenAndCheckAdminOrVetRole(String token) {
        TokenVerifyResponse response = authServiceClient.verifyToken(
                new TokenVerifyRequest(extractToken(token))
        );

        if (!"ADMIN".equals(response.role()) && !"VET".equals(response.role())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Admin or Vet role required");
        }
    }

    private String extractToken(String authHeader) {
        if (authHeader != null) {
            return authHeader;
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid authorization header");
    }
}
//    public ClinicDto addVetToClinic(String clinicId, String vetId) {
//        Profile clinic = clinicRepository.findByIdAndRole(clinicId, UserRole.CLINIC)
//                .orElseThrow(() -> new ServiceException(ErrorCode.NOT_FOUND, "Clinic not found"));
//
//        Profile vet = userRepository.findById(vetId)
//                .orElseThrow(() -> new ServiceException(ErrorCode.NOT_FOUND, "Vet not found"));
//
//        vet.setClinicId(clinicId);
//        userRepository.save(vet);
//
//        return mapToClinicDto(clinic);
//    }

//    public ClinicShortDto getClinic(String clinicId) {
//        Profile clinic = clinicRepository.findByIdAndRole(clinicId, UserRole.CLINIC)
//                .orElseThrow(() -> new ServiceException(ErrorCode.NOT_FOUND, "Clinic not found"));
//        return mapToClinicShortDto(clinic);
//    }
//
//    public List<ClinicShortDto> getAllClinics() {
//        return clinicRepository.findByRole(UserRole.CLINIC).stream()
//                .map(this::mapToClinicShortDto)
//                .collect(Collectors.toList());
//    }

//    private ClinicDto mapToClinicDto(Profile clinic) {
//        List<Profile> vets = userRepository.findByClinicId(Long.parseLong(clinic.getId()));
//
//        return ClinicDto.builder()
//                .id(clinic.getId())
//                .name(clinic.getClinicName())
//                .phone(clinic.getPhone())
//                .licenseNumber(clinic.getLicenseNumber())
//                .address(new AddressDto(
//                        clinic.getCity(),
//                        clinic.getStreet(),
//                        clinic.getBuilding()
//                ))
//                .logoUrl(clinic.getAvatarUrl())
//                .workingHours(clinic.getWorkingHours())
//                .vets(vets.stream().map(this::mapToVetShortDto).collect(Collectors.toList()))
//                .build();
//    }
//
//    private ClinicShortDto mapToClinicShortDto(Profile clinic) {
//        return ClinicShortDto.builder()
//                .id(clinic.getId())
//                .name(clinic.getClinicName())
//                .city(clinic.getCity())
//                .logoUrl(clinic.getAvatarUrl())
//                .build();
//    }
//
//    private ClinicDto.VetShortDto mapToVetShortDto(Profile vet) {
//        return ClinicDto.VetShortDto.builder()
//                .id(vet.getId())
//                .firstName(vet.getFirstName())
//                .lastName(vet.getLastName())
//                .specialization(vet.getSpecialization())
//                .avatarUrl(vet.getAvatarUrl())
//                .build();
//    }

