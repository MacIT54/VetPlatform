package com.vet.profile_service.core.repository;

import com.vet.profile_service.api.dto.UserRole;
import com.vet.profile_service.core.entity.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface ProfileRepository extends MongoRepository<Profile, String> {
    Optional<Profile> findByUserId(String userId);
    Optional<Profile> findByFirstName(String firstName);
    boolean existsByUserId(String userId);
    List<Profile> findAll();
}