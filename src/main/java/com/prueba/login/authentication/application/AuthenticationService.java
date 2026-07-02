package com.prueba.login.authentication.application;

import com.prueba.login.authentication.api.dto.request.LoginRequest;
import com.prueba.login.authentication.api.dto.response.AuthenticationResponse;
import com.prueba.login.authentication.internal.security.JwtTokenProvider;
import org.springframework.modulith.ApplicationModule;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Servicio de aplicación para la gestión de autenticación.
 */
@Service
@ApplicationModule
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthenticationService(AuthenticationManager authenticationManager, 
                                JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * Autentica un usuario y genera un token JWT.
     */
    public AuthenticationResponse authenticate(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenProvider.generateToken(authentication);

        return AuthenticationResponse.withToken(jwt);
    }
}
