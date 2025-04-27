package com.vet.auth_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.vet.auth_service.model.AuthUser;

import java.util.Optional;

@Repository
public interface IAuthUserRepository extends JpaRepository<AuthUser, Long> {
    Optional<AuthUser> findByLogin(String login);

    boolean existsByLogin(String login);

    boolean existsByEmail(String email);
}
