package com.prueba.login.authentication.api.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO para solicitud de autenticación (login).
 */
public record LoginRequest(
    @NotBlank(message = "El username no puede estar vacío")
    String username,
    
    @NotBlank(message = "La contraseña no puede estar vacía")
    String password
) {
}
