package com.vet.auth_service.service;

import com.vet.auth_service.api.dto.*;
import com.vet.auth_service.api.exception.UserBannedException;
import com.vet.auth_service.model.TokenSession;
import com.vet.auth_service.repository.ITokenSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.vet.auth_service.api.exception.InvalidPasswordException;
import com.vet.auth_service.api.exception.UserAlreadyExistsException;
import com.vet.auth_service.api.exception.UserNotFoundException;
import com.vet.auth_service.model.AuthUser;
import com.vet.auth_service.repository.IAuthUserRepository;
import com.vet.auth_service.security.jwt.JwtTokenProvider;

import java.time.LocalDateTime;

@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final IAuthUserRepository authUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final ITokenSessionRepository tokenSessionRepository;

    @Autowired
    public AuthService(JwtTokenProvider jwtTokenProvider,
                       AuthenticationManager authenticationManager,
                       IAuthUserRepository authUserRepository,
                       PasswordEncoder passwordEncoder,
                       ITokenSessionRepository tokenSessionRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
        this.authUserRepository = authUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenSessionRepository = tokenSessionRepository;
    }

    public SignupResponse signup(SignupRequest request) {
        if (authUserRepository.existsByLogin(request.getLogin())) {
            throw new UserAlreadyExistsException("User with login " + request.getLogin() + " already exists.");
        }

        AuthUser user = new AuthUser();
        user.setLogin(request.getLogin());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setSurname(request.getSurname());
        user.setUserType(request.getUserType());
        user.setEnabled(true);

        authUserRepository.save(user);

        return new SignupResponse("Signup Successful");
    }

    public JwtResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.login(), request.password())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            AuthUser authUser = authUserRepository.findByLogin(userDetails.getUsername())
                    .orElseThrow(() -> new UserNotFoundException("User with login " + userDetails.getUsername() + " not found."));

            if (!authUser.isEnabled()) {
                throw new UserBannedException("User is disabled");
            }

            String token = jwtTokenProvider.createToken(authUser);

            TokenSession session = TokenSession.builder()
                    .authUser(authUser)
                    .token(token)
                    .issuedAt(LocalDateTime.now())
                    .expiresAt(LocalDateTime.now().plusSeconds(jwtTokenProvider.getValidityInMilliseconds() / 1000))
                    .revoked(false)
                    .build();

            tokenSessionRepository.save(session);

            return new JwtResponse(token);

        } catch (BadCredentialsException e) {
            throw new RuntimeException("Invalid login or password", e);
        }
    }

    public ChangePasswordResponse changePassword(ChangePasswordRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        AuthUser user = authUserRepository.findByLogin(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.oldPassword(), user.getPassword())) {
            throw new InvalidPasswordException("Old password is incorrect.");
        }

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        authUserRepository.save(user);

        return new ChangePasswordResponse("Successfully changed password.");
    }
}
