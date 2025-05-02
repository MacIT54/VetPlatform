package ru.sibsutis.payments_service.api.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.sibsutis.payments_service.api.client.dto.TokenVerifyRequest;
import ru.sibsutis.payments_service.api.client.dto.TokenVerifyResponse;

@FeignClient(
        name = "auth-service",
        url = "${auth.service.url}",
        path = "/api/token"
)
public interface AuthServiceClient {
    @PostMapping("/verify")
    TokenVerifyResponse verifyToken(@RequestBody TokenVerifyRequest request);
}
