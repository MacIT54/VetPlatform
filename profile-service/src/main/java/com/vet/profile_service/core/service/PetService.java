package com.vet.profile_service.core.service;

import com.vet.profile_service.api.client.AuthServiceClient;
import com.vet.profile_service.api.client.entity.TokenVerifyRequest;
import com.vet.profile_service.api.client.entity.TokenVerifyResponse;
import com.vet.profile_service.api.dto.MedicalRecordDto;
import com.vet.profile_service.api.dto.PetDto;
import com.vet.profile_service.api.dto.PetRegistrationDto;
import com.vet.profile_service.api.dto.PetUpdateDto;
import com.vet.profile_service.api.dto.VetShortDto;
import com.vet.profile_service.core.entity.MedicalRecord;
import com.vet.profile_service.core.entity.Pet;
import com.vet.profile_service.core.entity.Profile;
import com.vet.profile_service.core.entity.Vet;
import com.vet.profile_service.core.exception.ErrorCode;
import com.vet.profile_service.core.exception.ServiceException;
import com.vet.profile_service.core.repository.MedicalRecordRepository;
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
    private final MedicalRecordRepository medicalRecordRepository;
    private final TokenVerifyService tokenVerifyService;

    public PetRegistrationDto addPet(PetRegistrationDto dto, String token) {
        TokenVerifyResponse authResponse = tokenVerifyService.tokenResponse(token);
        if (!authResponse.role().equals("USER")) {
            throw new ServiceException(ErrorCode.FORBIDDEN, "Only pet owners can add pets");
        }

        Profile owner = profileRepository.findByUserId(authResponse.username())
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

    public List<PetRegistrationDto> getMyPets(String token) {
        TokenVerifyResponse authResponse = tokenVerifyService.tokenResponse(token);

        Profile owner = profileRepository.findByUserId(authResponse.username())
                .orElseThrow(() -> new ServiceException(ErrorCode.NOT_FOUND, "Owner not found"));

        return petRepository.findByOwnerId(owner.getId()).stream()
                .map(this::mapToPetRegistrationDto)
                .toList();
    }

    public List<MedicalRecordDto> getMedicalRecords(String petId, String token) {

        return medicalRecordRepository.findByPetId(petId).stream()
                .map(this::mapToMedicalRecordDto)
                .toList();
    }

    public PetDto getPetById(String token, Long petId) {
        Pet pet = petRepository.findById(petId.toString())
                .orElseThrow(() -> new ServiceException(ErrorCode.NOT_FOUND, "Pet not found"));

        return mapToPetDto(pet);
    }

    public PetDto updatePet(String token, Long petId, PetUpdateDto updateDto) {
        Pet pet = petRepository.findById(petId.toString())
                .orElseThrow(() -> new ServiceException(ErrorCode.NOT_FOUND, "Pet not found"));

        pet.setName(updateDto.name());
        pet.setType(updateDto.type());
        pet.setBreed(updateDto.breed());
        pet.setBirthDate(updateDto.birthDate());
        pet.setChipNumber(updateDto.chipNumber());

        Pet updatedPet = petRepository.save(pet);
        return mapToPetDto(updatedPet);
    }

    public MedicalRecordDto addMedicalRecord(String petId, MedicalRecordDto dto, String token) {
        TokenVerifyResponse authResponse = tokenVerifyService.tokenResponse(token);
        if (!authResponse.role().equals("VET")) {
            throw new ServiceException(ErrorCode.FORBIDDEN, "Only vets can add medical records");
        }

        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new ServiceException(ErrorCode.NOT_FOUND, "Pet not found"));

        Vet vet = vetRepository.findById(dto.vet().id())
                .orElseThrow(() -> new ServiceException(ErrorCode.NOT_FOUND, "Vet not found"));

        if (!vet.getId().equals(authResponse.username())) {
            throw new ServiceException(ErrorCode.FORBIDDEN, "You can only add records as yourself");
        }

        MedicalRecord record = new MedicalRecord();
        record.setDiagnosis(dto.diagnosis());
        record.setTreatment(dto.treatment());
        record.setNotes(dto.notes());
        record.setDate(dto.date());
        record.setVet(vet);
        record.setPet(pet);

        MedicalRecord savedRecord = medicalRecordRepository.save(record);
        pet.getMedicalRecords().add(savedRecord);
        petRepository.save(pet);

        return mapToMedicalRecordDto(savedRecord);
    }

    private PetDto mapToPetDto(Pet pet) {
        List<MedicalRecordDto> medicalRecords = medicalRecordRepository
                .findByPetId(pet.getId()).stream()
                .map(this::mapToMedicalRecordDto)
                .toList();

        return new PetDto(
                pet.getId(),
                pet.getName(),
                pet.getType(),
                pet.getBreed(),
                pet.getBirthDate(),
                pet.getChipNumber(),
                medicalRecords
        );
    }


    private PetRegistrationDto mapToPetRegistrationDto(Pet pet) {
        return new PetRegistrationDto(
                pet.getName(),
                pet.getType(),
                pet.getBreed(),
                pet.getBirthDate(),
                pet.getChipNumber()
        );
    }

    private MedicalRecordDto mapToMedicalRecordDto(MedicalRecord record) {
        return new MedicalRecordDto(
                record.getId(),
                record.getDiagnosis(),
                record.getTreatment(),
                record.getNotes(),
                record.getDate(),
                new VetShortDto(
                        record.getVet().getId(),
                        record.getVet().getFirstName(),
                        record.getVet().getLastName(),
                        record.getVet().getSpecialization(),
                        record.getVet().getAvatarUrl()
                )
        );
    }

}