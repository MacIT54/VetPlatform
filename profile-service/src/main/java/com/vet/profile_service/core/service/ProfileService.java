package com.vet.profile_service.core.service;

import com.vet.profile_service.api.client.entity.TokenVerifyRequest;
import com.vet.profile_service.api.client.entity.TokenVerifyResponse;
import com.vet.profile_service.api.dto.AddressDto;
import com.vet.profile_service.api.dto.ClinicShortDto;
import com.vet.profile_service.api.dto.PetShortDto;
import com.vet.profile_service.api.dto.UserProfileDto;
import com.vet.profile_service.api.dto.UserProfileUpdateDto;
import com.vet.profile_service.api.dto.UserRegistrationDto;
import com.vet.profile_service.api.dto.UserRole;
import com.vet.profile_service.api.dto.VetDto;
import com.vet.profile_service.core.entity.Profile;
import com.vet.profile_service.core.entity.Vet;
import com.vet.profile_service.core.exception.ErrorCode;
import com.vet.profile_service.core.exception.ServiceException;
import com.vet.profile_service.core.repository.PetRepository;
import com.vet.profile_service.core.repository.ProfileRepository;
import com.vet.profile_service.core.repository.VetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final VetRepository vetRepository;
    private final TokenVerifyService tokenVerifyService;
    private final PetRepository petRepository;

    public UserProfileDto getCurrentUserProfile(String token) {
        TokenVerifyResponse tokenVerifyResponse = tokenVerifyService.tokenResponse(token);
        Profile profile = profileRepository.findByUserId(tokenVerifyResponse.id())
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

    public void createProfile(UserRegistrationDto userDto) {
        if (profileRepository.existsByUserId(userDto.userId())) {
            throw new ServiceException(ErrorCode.CONFLICT, "Profile already exists");
        }

        Profile profile = new Profile();
        profile.setUserId(userDto.userId());
        profile.setFirstName(userDto.firstName());
        profile.setLastName(userDto.lastName());
        profile.setEmail(userDto.email());

        profileRepository.save(profile);
    }

    public UserProfileDto updateCurrentUserProfile(UserProfileUpdateDto updateDto, String token) {
        String tokenResponse = tokenVerifyService.extractToken(token);

        Profile profile = profileRepository.findByUserId(tokenResponse)
                .orElseThrow(() -> new ServiceException(ErrorCode.NOT_FOUND, "Profile not found"));

        profile.setFirstName(updateDto.firstName());
        profile.setLastName(updateDto.lastName());
        profile.setPhone(updateDto.phone());
        profile.setCity(updateDto.address().city());
        profile.setStreet(updateDto.address().street());
        profile.setBuilding(updateDto.address().building());
        profile.setAvatarUrl(updateDto.avatarUrl());

        Profile updatedProfile = profileRepository.save(profile);

        return mapToUserProfileDto(updatedProfile);
    }

    private UserProfileDto mapToUserProfileDto(Profile profile) {
        List<PetShortDto> petDtos = petRepository.findByOwnerId(profile.getId()).stream()
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
                vet.getEmail(),
                vet.getAvatarUrl(),
                clinicDto,
                vet.getServices(),
                new AddressDto(vet.getCity(), vet.getStreet(), vet.getBuilding())
        );
    }

    public List<UserProfileDto> getAllUsers(int page, int size) {
        return profileRepository.findAll(PageRequest.of(page, size)).stream()
                .map(p -> new UserProfileDto(
                        p.getId(),
                        p.getUserId(),
                        p.getFirstName(),
                        p.getLastName(),
                        p.getPhone(),
                        p.getAvatarUrl(),
                        new AddressDto(p.getCity(), p.getStreet(), p.getBuilding()),
                        p.getPets().stream()
                                .map(pet -> new PetShortDto(pet.getId(), pet.getName(), pet.getType()))
                                .toList()
                ))
                .toList();
    }

}