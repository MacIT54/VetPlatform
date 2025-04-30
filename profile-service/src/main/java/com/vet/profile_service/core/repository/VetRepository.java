package com.vet.profile_service.core.repository;

import com.vet.profile_service.core.entity.Vet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface VetRepository extends MongoRepository<Vet, String> {
    Optional<Vet> findById(String id);
    List<Vet> findByClinicId(String clinicId);
    List<Vet> findBySpecializationAndCity(String specialization, String city);
    List<Vet> findByCity(String city);
    List<Vet> findAll();
    List<Vet> findBySpecialization(String specialization);
    List<Vet> findByClinicIsNull();
}
