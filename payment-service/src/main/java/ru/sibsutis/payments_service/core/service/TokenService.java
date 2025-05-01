package ru.sibsutis.payments_service.core.service;

import feign.FeignException;
import org.springframework.stereotype.Service;
import ru.sibsutis.payments_service.api.client.AuthServiceClient;
import ru.sibsutis.payments_service.api.client.dto.TokenVerifyRequest;
import ru.sibsutis.payments_service.api.client.dto.TokenVerifyResponse;
import ru.sibsutis.payments_service.core.exception.ForbiddenException;
import ru.sibsutis.payments_service.core.exception.PaymentException;
import ru.sibsutis.payments_service.core.exception.UnauthorizedException;

@Service
public class TokenService {

    private final AuthServiceClient authServiceClient;

    public TokenService(AuthServiceClient authServiceClient) {
        this.authServiceClient = authServiceClient;
    }

    public TokenVerifyResponse validateToken(String token) {
        try {
            TokenVerifyResponse verifyResponse = authServiceClient.verifyToken(new TokenVerifyRequest(token));

            if (verifyResponse == null) {
                throw new UnauthorizedException("Token verification failed: empty response");
            }

            if (!verifyResponse.enabled()) {
                throw new ForbiddenException("User account is disabled");
            }

            return verifyResponse;

        } catch (FeignException.FeignClientException e) {
            if (e.status() == 401) {
                throw new UnauthorizedException("Invalid token or unauthorized access");
            } else if (e.status() == 403) {
                throw new ForbiddenException("Access forbidden for this user");
            }
            throw new UnauthorizedException("Auth service client error: " + e.getMessage());
        } catch (FeignException e) {
            throw new RuntimeException("Auth service is unavailable: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error during token validation: " + e.getMessage());
        }
    }
}

