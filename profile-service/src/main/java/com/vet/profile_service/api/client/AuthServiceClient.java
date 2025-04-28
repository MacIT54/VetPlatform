package com.vet.profile_service.api.client;

import com.vet.profile_service.api.client.entity.TokenVerifyRequest;
import com.vet.profile_service.api.client.entity.TokenVerifyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "auth-service",
        url = "${auth.service.url}",
        path = "/api/token"
)
public interface AuthServiceClient {
    @PostMapping("/verify")
    TokenVerifyResponse verifyToken(
            @RequestBody TokenVerifyRequest request
    );
}