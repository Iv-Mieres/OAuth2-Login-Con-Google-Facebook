package com.prueba.login.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CustomerUserDtoReq (

    @Email(message = "Debe contener un formato de tipo email: example@example.com")
    String email,
    @NotNull(message = "No puede estar vacio.")
    String name,
    @NotNull(message = "No puede estar vacio.")
    String surname,
    String picture,
    @NotNull(message = "No puede estar vacio.")
    @Size(min = 8, message = "Debe contener un mínimo de 8 caracteres.")
    String password,
    @NotNull(message = "No puede estar vacio.")
    @Size(min = 8, message = "Debe contener un mínimo de 8 caracteres.")
    String repeatPassword){
}
