package com.prueba.login.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AuthLoginDtoReq(@NotBlank(message = "no puede estar vacio") String username,
                              @NotBlank(message = "no puede estar vacio") String password) {
}
