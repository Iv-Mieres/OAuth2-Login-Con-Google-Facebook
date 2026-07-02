package com.prueba.login.user.internal.exceptions;

/**
 * Excepción lanzada cuando una solicitud es inválida o malformada.
 */
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
