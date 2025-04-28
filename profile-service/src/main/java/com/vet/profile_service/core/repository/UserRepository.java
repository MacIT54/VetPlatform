package com.vet.profile_service.core.repository;

import com.vet.profile_service.core.entity.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<Profile, String> {
    Page<Profile> findAll(Pageable pageable);
}