package com.prueba.login.user.internal.exceptions;

/**
 * Excepción lanzada cuando un recurso solicitado no se encuentra.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
