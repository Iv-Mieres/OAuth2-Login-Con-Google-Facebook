package com.prueba.login.authentication.api;

import com.prueba.login.authentication.api.dto.request.LoginRequest;
import com.prueba.login.authentication.api.dto.response.AuthenticationResponse;
import com.prueba.login.authentication.application.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST para la gestión de autenticación.
 */
@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * Autentica un usuario y devuelve un token JWT.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthenticationResponse response = authenticationService.authenticate(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
