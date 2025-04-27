package com.vet.auth_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.vet.auth_service.api.dto.ChangePasswordRequest;
import com.vet.auth_service.api.dto.JwtResponse;
import com.vet.auth_service.api.dto.LoginRequest;
import com.vet.auth_service.api.dto.SignupRequest;
import com.vet.auth_service.api.exception.InvalidPasswordException;
import com.vet.auth_service.api.exception.UserAlreadyExistsException;
import com.vet.auth_service.api.exception.UserNotFoundException;
import com.vet.auth_service.model.AuthUser;
import com.vet.auth_service.repository.IAuthUserRepository;
import com.vet.auth_service.security.jwt.JwtTokenProvider;

@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final IAuthUserRepository authUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(JwtTokenProvider jwtTokenProvider,
                       AuthenticationManager authenticationManager,
                       IAuthUserRepository authUserRepository,
                       PasswordEncoder passwordEncoder) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
        this.authUserRepository = authUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void signup(SignupRequest request) {
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
    }

    public JwtResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.login(), request.password())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtTokenProvider.createToken(userDetails.getUsername());

            return new JwtResponse(token);
        } catch (BadCredentialsException e) {
            throw new RuntimeException("Invalid login or password", e);
        }
    }

    public void changePassword(ChangePasswordRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        AuthUser user = authUserRepository.findByLogin(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.oldPassword(), user.getPassword())) {
            throw new InvalidPasswordException("Old password is incorrect.");
        }

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        authUserRepository.save(user);
    }
}
