package com.authapi.exception;

/**
 * Custom Authentication Exception
 * 
 * Thrown when authentication fails due to invalid credentials,
 * unauthorized access, or other authentication-related issues.
 */
public class AuthenticationException extends RuntimeException {

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}