package com.vet.auth_service.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class UserCreatedEvent extends UserEvent {
    private String email;
    private String firstName;
    private String lastName;
}