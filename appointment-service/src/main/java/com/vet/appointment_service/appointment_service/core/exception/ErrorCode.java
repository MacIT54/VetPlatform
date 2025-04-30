package com.vet.appointment_service.appointment_service.core.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    BAD_REQUEST(400, "Некорректный запрос"),

    NOT_FOUND(404, "Ресурс не найден"),

    INTERNAL_SERVER_ERROR(500, "Внутренняя ошибка сервера"),

    UNAUTHORIZED(401, "Неавторизованный доступ"),

    FORBIDDEN(403, "Доступ запрещен"),

    CONFLICT(409, "Конфликт данных");

    private final int status;

    private final String description;
}
