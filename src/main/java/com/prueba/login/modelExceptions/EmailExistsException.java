package com.prueba.login.modelExceptions;

public class EmailExistsException extends RuntimeException{

    public EmailExistsException(String message) {
        super(message);
    }
}
