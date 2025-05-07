package com.vet.auth_service.service;

import com.vet.auth_service.model.UserCreationFailedEvent;
import com.vet.auth_service.repository.IAuthUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserSagaCompensationHandler {
    private final AuthService authService;
    private final IAuthUserRepository authUserRepository;

    @KafkaListener(topics = "user-creation-failed", groupId = "auth-service-group")
    @Transactional
    public void handleUserCreationFailed(UserCreationFailedEvent event) {
        authUserRepository.deleteById(Long.parseLong(event.getUserId()));
    }
}
