package com.vet.profile_service.core.service;

import com.vet.profile_service.api.dto.AddressDto;
import com.vet.profile_service.api.dto.ClinicShortDto;
import com.vet.profile_service.api.dto.PetShortDto;
import com.vet.profile_service.api.dto.UserProfileDto;
import com.vet.profile_service.api.dto.UserProfileUpdateDto;
import com.vet.profile_service.api.dto.UserRole;
import com.vet.profile_service.api.dto.VetDto;
import com.vet.profile_service.core.entity.Profile;
import com.vet.profile_service.core.entity.Vet;
import com.vet.profile_service.core.exception.ErrorCode;
import com.vet.profile_service.core.exception.ServiceException;
import com.vet.profile_service.core.repository.ProfileRepository;
import com.vet.profile_service.core.repository.VetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final VetRepository vetRepository;

    public UserProfileDto getCurrentUserProfile() {
        Profile profile = profileRepository.findByUserId("current-user-id")
                .orElseThrow(() -> new ServiceException(ErrorCode.NOT_FOUND, "Profile not found"));

        List<PetShortDto> petDtos = profile.getPets().stream()
                .map(p -> new PetShortDto(p.getId(), p.getName(), p.getType()))
                .toList();

        return new UserProfileDto(
                profile.getId(),
                profile.getUserId(),
                profile.getFirstName(),
                profile.getLastName(),
                profile.getPhone(),
                profile.getAvatarUrl(),
                new AddressDto(
                        profile.getCity(),
                        profile.getStreet(),
                        profile.getBuilding()
                ),
                petDtos
        );
    }

    public List<VetDto> searchVets(String specialization, String city) {
        List<Vet> vets;
        if (specialization != null && city != null) {
            vets = vetRepository.findBySpecializationAndCity(specialization, city);
        } else if (specialization != null) {
            vets = vetRepository.findBySpecialization(specialization);
        } else if (city != null) {
            vets = vetRepository.findByCity(city);
        } else {
            vets = vetRepository.findAll();
        }

        return vets.stream()
                .map(this::mapToVetDto)
                .toList();
    }

    private VetDto mapToVetDto(Vet vet) {
        ClinicShortDto clinicDto = vet.getClinic() != null
                ? new ClinicShortDto(vet.getClinic().getId(), vet.getClinic().getName(), vet.getClinic().getLogoUrl())
                : null;

        return new VetDto(
                vet.getId(),
                vet.getFirstName(),
                vet.getLastName(),
                vet.getSpecialization(),
                vet.getQualification(),
                vet.getBio(),
                vet.getAvatarUrl(),
                clinicDto,
                vet.getServices(),
                new AddressDto(vet.getCity(), vet.getStreet(), vet.getBuilding())
        );
    }
}