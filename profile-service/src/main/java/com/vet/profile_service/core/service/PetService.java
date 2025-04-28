package com.vet.profile_service.core.service;

import com.vet.profile_service.api.dto.MedicalRecordDto;
import com.vet.profile_service.api.dto.PetRegistrationDto;
import com.vet.profile_service.api.dto.VetShortDto;
import com.vet.profile_service.core.entity.MedicalRecord;
import com.vet.profile_service.core.entity.Pet;
import com.vet.profile_service.core.entity.Profile;
import com.vet.profile_service.core.entity.Vet;
import com.vet.profile_service.core.exception.ErrorCode;
import com.vet.profile_service.core.exception.ServiceException;
import com.vet.profile_service.core.repository.PetRepository;
import com.vet.profile_service.core.repository.ProfileRepository;
import com.vet.profile_service.core.repository.UserRepository;
import com.vet.profile_service.core.repository.VetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PetService {
    private final PetRepository petRepository;
    private final ProfileRepository profileRepository;
    private final VetRepository vetRepository;

    public PetRegistrationDto addPet(PetRegistrationDto dto) {
        Profile owner = profileRepository.findByUserId("current-user-id")
                .orElseThrow(() -> new ServiceException(ErrorCode.NOT_FOUND, "Owner not found"));

        Pet pet = new Pet();
        pet.setName(dto.name());
        pet.setType(dto.type());
        pet.setBreed(dto.breed());
        pet.setBirthDate(dto.birthDate());
        pet.setChipNumber(dto.chipNumber());
        pet.setOwner(owner);

        Pet savedPet = petRepository.save(pet);
        owner.getPets().add(savedPet);
        profileRepository.save(owner);

        return new PetRegistrationDto(
                savedPet.getName(),
                savedPet.getType(),
                savedPet.getBreed(),
                savedPet.getBirthDate(),
                savedPet.getChipNumber()
        );
    }

    public List<PetRegistrationDto> getMyPets(String ownerId) {
        return petRepository.findByOwnerId(ownerId).stream()
                .map(p -> new PetRegistrationDto(
                        p.getName(),
                        p.getType(),
                        p.getBreed(),
                        p.getBirthDate(),
                        p.getChipNumber()
                ))
                .toList();
    }

    public MedicalRecordDto addMedicalRecord(String petId, MedicalRecordDto dto) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new ServiceException(ErrorCode.NOT_FOUND, "Pet not found"));

        Vet vet = vetRepository.findById(dto.vet().id())
                .orElseThrow(() -> new ServiceException(ErrorCode.NOT_FOUND, "Vet not found"));

        MedicalRecord record = new MedicalRecord();
        record.setDiagnosis(dto.diagnosis());
        record.setTreatment(dto.treatment());
        record.setNotes(dto.notes());
        record.setDate(dto.date());
        record.setVet(vet);
        record.setPet(pet);

        pet.getMedicalRecords().add(record);
        petRepository.save(pet);

        return new MedicalRecordDto(
                record.getId(),
                record.getDiagnosis(),
                record.getTreatment(),
                record.getNotes(),
                record.getDate(),
                new VetShortDto(
                        vet.getId(),
                        vet.getFirstName(),
                        vet.getLastName(),
                        vet.getSpecialization(),
                        vet.getAvatarUrl()
                )
        );
    }
}