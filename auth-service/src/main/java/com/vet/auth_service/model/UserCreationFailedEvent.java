package com.vet.auth_service.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class UserCreationFailedEvent extends UserEvent {
    private String reason;
}
