package com.prueba.login.modelExceptions;

public class RoleNameNotFoundException extends RuntimeException{
    public RoleNameNotFoundException(String message) {
        super(message);
    }
}
