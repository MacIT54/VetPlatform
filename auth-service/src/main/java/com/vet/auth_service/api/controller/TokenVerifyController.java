package com.vet.auth_service.api.controller;

import com.vet.auth_service.api.dto.TokenVerifyRequest;
import com.vet.auth_service.api.dto.TokenVerifyResponse;
import com.vet.auth_service.service.TokenVerifyService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/token/")
@RequiredArgsConstructor
public class TokenVerifyController {
    private final TokenVerifyService tokenVerifyService;

    @Hidden
    @PostMapping("/verify")
    public TokenVerifyResponse verifyToken(@RequestBody TokenVerifyRequest request) {
        return tokenVerifyService.verifyToken(request);
    }
}
