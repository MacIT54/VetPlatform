package com.vet.auth_service.repository;

import com.vet.auth_service.model.TokenSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ITokenSessionRepository extends JpaRepository<TokenSession, Long> {
    Optional<TokenSession> findByToken(String token);
}
