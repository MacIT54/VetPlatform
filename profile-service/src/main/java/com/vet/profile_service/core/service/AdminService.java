package com.vet.profile_service.core.service;

import com.vet.profile_service.api.dto.AddressDto;
import com.vet.profile_service.api.dto.PetShortDto;
import com.vet.profile_service.api.dto.UserProfileDto;
import com.vet.profile_service.api.dto.UserRole;
import com.vet.profile_service.core.entity.Clinic;
import com.vet.profile_service.core.entity.Profile;
import com.vet.profile_service.core.exception.ErrorCode;
import com.vet.profile_service.core.exception.ServiceException;
import com.vet.profile_service.core.repository.ClinicRepository;
import com.vet.profile_service.core.repository.ProfileRepository;
import com.vet.profile_service.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final ProfileRepository profileRepository;
    private final ClinicRepository clinicRepository;

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

    public void deleteClinic(String clinicId) {
        Clinic clinic = clinicRepository.findByIdAndDeletedFalse(clinicId)
                .orElseThrow(() -> new ServiceException(ErrorCode.NOT_FOUND, "Clinic not found"));
        clinic.setDeleted(true);
        clinicRepository.save(clinic);
    }
}