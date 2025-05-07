package com.vet.profile_service.core.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class UserCreationFailedEvent extends UserEvent {
    private String reason;
}
