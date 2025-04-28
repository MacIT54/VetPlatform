package com.vet.profile_service.core.repository;

import com.vet.profile_service.api.dto.UserRole;
import com.vet.profile_service.core.entity.Clinic;
import com.vet.profile_service.core.entity.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface ClinicRepository extends MongoRepository<Clinic, String> {
    Optional<Clinic> findByIdAndDeletedFalse(String id);
    List<Clinic> findAllByDeletedFalse();
    Page<Clinic> findByServicesContainingAndDeletedFalse(String service, Pageable pageable);
    Page<Clinic> findByCityAndDeletedFalse(String city, Pageable pageable);
    @Query("{'vets._id': ?0, 'deleted': false}")
    Optional<Clinic> findClinicByVetId(String vetId);
}