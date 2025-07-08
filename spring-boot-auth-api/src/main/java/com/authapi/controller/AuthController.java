package com.authapi.controller;

import com.authapi.dto.ApiResponse;
import com.authapi.dto.AuthResponse;
import com.authapi.dto.LoginRequest;
import com.authapi.dto.RegisterRequest;
import com.authapi.exception.AuthenticationException;
import com.authapi.exception.UserAlreadyExistsException;
import com.authapi.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Authentication REST Controller
 * 
 * Provides endpoints for user authentication including:
 * - User registration with BCrypt password encryption
 * - User login with encrypted password verification
 * - JWT token generation and management
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * User Registration Endpoint
     * 
     * Registers a new user with encrypted password using BCrypt algorithm.
     * Validates input data and returns JWT token upon successful registration.
     * 
     * @param registerRequest the registration request containing user details
     * @return ResponseEntity with authentication response including JWT token
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @Valid @RequestBody RegisterRequest registerRequest) {
        
        try {
            // Validate password strength
            if (!authService.isPasswordSecure(registerRequest.getPassword())) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Password must be at least 8 characters long and contain uppercase, lowercase, digit, and special character"));
            }

            // Register user with encrypted password
            AuthResponse authResponse = authService.register(registerRequest);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("User registered successfully", authResponse));
                    
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error(e.getMessage()));
                    
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
                    
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Registration failed: " + e.getMessage()));
        }
    }

    /**
     * User Login Endpoint
     * 
     * Authenticates user credentials using BCrypt password verification.
     * Returns JWT token upon successful authentication.
     * 
     * @param loginRequest the login request containing username/email and password
     * @return ResponseEntity with authentication response including JWT token
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest loginRequest) {
        
        try {
            // Authenticate user with encrypted password verification
            AuthResponse authResponse = authService.login(loginRequest);
            
            return ResponseEntity.ok()
                    .body(ApiResponse.success("Login successful", authResponse));
                    
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(e.getMessage()));
                    
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Login failed: " + e.getMessage()));
        }
    }

    /**
     * Password Validation Endpoint
     * 
     * Checks if a password meets security requirements.
     * Useful for frontend password strength indicators.
     * 
     * @param password the password to validate
     * @return ResponseEntity indicating if password is secure
     */
    @PostMapping("/validate-password")
    public ResponseEntity<ApiResponse<Boolean>> validatePassword(@RequestBody String password) {
        
        try {
            boolean isSecure = authService.isPasswordSecure(password);
            String message = isSecure ? 
                    "Password meets security requirements" : 
                    "Password must be at least 8 characters with uppercase, lowercase, digit, and special character";
                    
            return ResponseEntity.ok()
                    .body(ApiResponse.success(message, isSecure));
                    
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Password validation failed: " + e.getMessage()));
        }
    }

    /**
     * Health Check Endpoint
     * 
     * Simple endpoint to check if the authentication service is running.
     * 
     * @return ResponseEntity with health status
     */
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> health() {
        return ResponseEntity.ok()
                .body(ApiResponse.success("Authentication service is running", "OK"));
    }
}