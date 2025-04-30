package com.vet.profile_service.core.repository;

import com.vet.profile_service.core.entity.MedicalRecord;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MedicalRecordRepository extends MongoRepository<MedicalRecord, String> {
    List<MedicalRecord> findByPetId(String petId);
}