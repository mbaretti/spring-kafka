package com.authapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the Authentication REST API
 * 
 * Features:
 * - JWT Authentication
 * - User registration and login
 * - Clean architecture with separated layers
 * - Comprehensive security configuration
 * - Input validation and error handling
 */
@SpringBootApplication
public class AuthApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthApiApplication.class, args);
    }
}