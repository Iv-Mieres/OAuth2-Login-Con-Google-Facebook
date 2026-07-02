package com.prueba.login.user.api.dto.response;

import java.time.LocalDateTime;

/**
 * DTO para la respuesta de datos de usuario.
 */
public record UserResponse(
    Long id,
    String email,
    String name,
    String surname,
    String picture,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    // Constructor simplificado sin timestamps para compatibilidad
    public UserResponse(Long id, String email, String name, String surname, String picture) {
        this(id, email, name, surname, picture, null, null);
    }
}
