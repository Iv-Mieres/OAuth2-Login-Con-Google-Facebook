package com.prueba.login.user.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO para crear un nuevo usuario.
 */
public record CreateUserRequest(
    @NotBlank(message = "El nombre no puede estar vacío")
    String name,
    
    @NotBlank(message = "El apellido no puede estar vacío")
    String surname,
    
    @Email(message = "Debe contener un formato de email válido: example@example.com")
    @NotBlank(message = "El email no puede estar vacío")
    String email,
    
    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 8, message = "La contraseña debe contener al menos 8 caracteres")
    String password,
    
    @NotBlank(message = "La confirmación de contraseña no puede estar vacía")
    @Size(min = 8, message = "La confirmación de contraseña debe contener al menos 8 caracteres")
    String repeatPassword
) {
}
