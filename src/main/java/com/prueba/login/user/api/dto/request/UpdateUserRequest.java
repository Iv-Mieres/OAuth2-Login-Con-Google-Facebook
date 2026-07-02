package com.prueba.login.user.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

/**
 * Solicitud para actualizar un usuario existente.
 * Los campos opcionales permiten actualizaciones parciales.
 */
public record UpdateUserRequest(
    @Size(min = 3, max = 50, message = "El nombre de usuario debe tener entre 3 y 50 caracteres")
    String username,
    
    @Email(message = "Debe contener un formato de email válido: example@example.com")
    String email,
    
    @Size(min = 8, max = 100, message = "La contraseña debe tener entre 8 y 100 caracteres")
    String password,
    
    Boolean enabled
) {
}
