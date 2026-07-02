package com.prueba.login.authentication.api.dto.response;

/**
 * DTO para la respuesta de autenticación.
 */
public record AuthenticationResponse(
    String username,
    String message,
    String jwt,
    Boolean status
) {
}
