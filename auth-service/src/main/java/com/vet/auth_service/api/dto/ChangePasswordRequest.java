package com.vet.auth_service.api.dto;

public record ChangePasswordRequest(String oldPassword, String newPassword) {}