package com.prueba.login.user.api.dto.response;

import java.util.Set;

/**
 * Respuesta con datos de usuario.
 */
public record UserResponse(
    Long id,
    String username,
    String email,
    Boolean enabled,
    Set<String> roles,
    Set<String> permissions,
    String authProvider,
    String providerId
) {
}
