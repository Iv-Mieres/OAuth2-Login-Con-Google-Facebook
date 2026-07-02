package com.prueba.login.authentication.api.dto.response;

import java.util.Set;

/**
 * Respuesta de autenticación exitosa con token JWT.
 */
public record AuthenticationResponse(
        String accessToken,
        String tokenType,
        Long expiresIn
) {
    /**
     * Constructor estático para crear una respuesta básica con solo el token.
     */
    public static AuthenticationResponse withToken(String accessToken) {
        return new AuthenticationResponse(accessToken, "Bearer", 86400L);
    }
}
