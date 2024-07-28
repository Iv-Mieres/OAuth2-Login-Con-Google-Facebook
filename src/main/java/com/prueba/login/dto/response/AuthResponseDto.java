package com.prueba.login.dto.response;

public record AuthResponseDto(String username, String message, String jwt, Boolean status) {
}
