package com.vet.profile_service.core.service;

import com.vet.profile_service.api.dto.UserRegistrationDto;
import com.vet.profile_service.core.entity.UserCreatedEvent;
import com.vet.profile_service.core.entity.UserCreationFailedEvent;
import com.vet.profile_service.core.entity.UserEvent;
import com.vet.profile_service.core.entity.UserProfileCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserSagaHandler {
    private final ProfileService profileService;
    private final KafkaTemplate<String, UserEvent> kafkaTemplate;

    @KafkaListener(topics = "user-created", groupId = "profile-service-group")
    public void handleUserCreated(UserCreatedEvent event) {
        try {
            UserRegistrationDto dto = UserRegistrationDto.builder()
                    .userId(event.getUserId())
                    .email(event.getEmail())
                    .lastName(event.getLastName())
                    .firstName(event.getFirstName())
                    .build();

            profileService.createProfile(dto);

            UserProfileCreatedEvent successEvent = new UserProfileCreatedEvent();
            successEvent.setEventId(UUID.randomUUID().toString());
            successEvent.setTimestamp(LocalDateTime.now());
            successEvent.setUserId(event.getUserId());

            kafkaTemplate.send("user-profile-created", successEvent);
        } catch (Exception e) {
            UserCreationFailedEvent failedEvent = new UserCreationFailedEvent();
            failedEvent.setEventId(UUID.randomUUID().toString());
            failedEvent.setTimestamp(LocalDateTime.now());
            failedEvent.setUserId(event.getUserId());
            failedEvent.setReason(e.getMessage());

            kafkaTemplate.send("user-creation-failed", failedEvent);
        }
    }
}
