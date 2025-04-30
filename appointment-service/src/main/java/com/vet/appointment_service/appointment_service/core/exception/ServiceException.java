package com.vet.appointment_service.appointment_service.core.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ServiceException extends RuntimeException {

    private final ErrorCode errorCode;

    private final String message;

    private final Throwable throwable;

    public ServiceException(ErrorCode errorCode, String message) {
        this(errorCode, message, null);
    }

}