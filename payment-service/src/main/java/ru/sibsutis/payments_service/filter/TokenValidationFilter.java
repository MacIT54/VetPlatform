package ru.sibsutis.payments_service.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.sibsutis.payments_service.api.client.dto.TokenVerifyResponse;
import ru.sibsutis.payments_service.core.exception.UnauthorizedException;
import ru.sibsutis.payments_service.core.service.TokenService;

import java.io.IOException;

@Component
public class TokenValidationFilter extends OncePerRequestFilter {

    private final TokenService tokenService;

    public TokenValidationFilter(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("Missing or invalid Authorization header");
        }

        String token = authorizationHeader.substring(7);

        TokenVerifyResponse verifyResponse = tokenService.validateToken(token);

        request.setAttribute("userLogin", verifyResponse.login());
        request.setAttribute("userRole", verifyResponse.role());
        request.setAttribute("userName", verifyResponse.name());
        request.setAttribute("userSurname", verifyResponse.surname());
        request.setAttribute("userEmail", verifyResponse.email());

        filterChain.doFilter(request, response);
    }
}

