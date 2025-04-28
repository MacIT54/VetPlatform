package com.vet.profile_service.core.repository;

import com.vet.profile_service.core.entity.MedicalRecord;
import com.vet.profile_service.core.entity.Pet;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface PetRepository extends MongoRepository<Pet, String> {
    List<Pet> findByOwnerId(String ownerId);
    Optional<Pet> findByIdAndOwnerId(String petId, String ownerId);
}