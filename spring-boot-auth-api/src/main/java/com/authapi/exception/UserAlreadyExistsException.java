package com.authapi.exception;

/**
 * User Already Exists Exception
 * 
 * Thrown when attempting to register a user with a username or email
 * that already exists in the system.
 */
public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String message) {
        super(message);
    }

    public UserAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}