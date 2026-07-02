package com.prueba.login.user.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

/**
 * DTO para actualizar un usuario existente.
 * Los campos opcionales permiten actualizaciones parciales.
 */
public record UpdateUserRequest(
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    String name,
    
    @Size(max = 100, message = "El apellido no puede superar los 100 caracteres")
    String surname,
    
    @Email(message = "Debe contener un formato de email válido: example@example.com")
    String email,
    
    @Size(max = 500, message = "La URL de la imagen no puede superar los 500 caracteres")
    String picture
) {
}
