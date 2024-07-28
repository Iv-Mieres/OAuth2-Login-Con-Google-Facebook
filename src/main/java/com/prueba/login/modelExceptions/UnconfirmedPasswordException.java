package com.prueba.login.modelExceptions;

public class UnconfirmedPasswordException extends RuntimeException{

    public UnconfirmedPasswordException(String message) {
        super(message);
    }
}
