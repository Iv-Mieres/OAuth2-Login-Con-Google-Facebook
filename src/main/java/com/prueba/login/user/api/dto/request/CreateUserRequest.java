package com.prueba.login.user.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Solicitud para crear un nuevo usuario.
 */
public record CreateUserRequest(
    @NotBlank(message = "El nombre de usuario no puede estar vacío")
    @Size(min = 3, max = 50, message = "El nombre de usuario debe tener entre 3 y 50 caracteres")
    String username,
    
    @Email(message = "Debe contener un formato de email válido: example@example.com")
    @NotBlank(message = "El email no puede estar vacío")
    String email,
    
    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 8, message = "La contraseña debe contener al menos 8 caracteres")
    String password
) {
}
