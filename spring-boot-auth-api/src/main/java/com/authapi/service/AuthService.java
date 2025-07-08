package com.authapi.service;

import com.authapi.dto.AuthResponse;
import com.authapi.dto.LoginRequest;
import com.authapi.dto.RegisterRequest;
import com.authapi.entity.User;
import com.authapi.exception.AuthenticationException;
import com.authapi.exception.UserAlreadyExistsException;
import com.authapi.repository.UserRepository;
import com.authapi.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Authentication Service
 * 
 * Handles user registration and authentication with secure password encryption.
 * Uses BCrypt algorithm for password hashing and JWT for session management.
 */
@Service
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuthenticationManager authenticationManager
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Register a new user with encrypted password
     * 
     * @param request the registration request
     * @return authentication response with JWT token
     * @throws UserAlreadyExistsException if username or email already exists
     */
    public AuthResponse register(RegisterRequest request) {
        // Validate that passwords match
        if (!request.isPasswordMatching()) {
            throw new AuthenticationException("Passwords do not match");
        }

        // Check if username already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("Username is already taken: " + request.getUsername());
        }

        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email is already registered: " + request.getEmail());
        }

        // Create new user with encrypted password
        User user = new User(
                request.getUsername(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()) // BCrypt encryption
        );

        // Save user to database
        User savedUser = userRepository.save(user);

        // Generate JWT token
        String jwtToken = jwtService.generateToken(savedUser);

        return AuthResponse.of(jwtToken, savedUser);
    }

    /**
     * Authenticate user login with encrypted password verification
     * 
     * @param request the login request
     * @return authentication response with JWT token
     * @throws AuthenticationException if credentials are invalid
     */
    public AuthResponse login(LoginRequest request) {
        try {
            // Authenticate user credentials
            // This will use the encrypted password verification
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            // Get authenticated user
            User user = (User) authentication.getPrincipal();

            // Generate JWT token
            String jwtToken = jwtService.generateToken(user);

            return AuthResponse.of(jwtToken, user);

        } catch (BadCredentialsException e) {
            throw new AuthenticationException("Invalid username or password");
        }
    }

    /**
     * Change user password with encryption
     * 
     * @param username the username
     * @param oldPassword the current password
     * @param newPassword the new password
     * @throws AuthenticationException if old password is incorrect
     */
    public void changePassword(String username, String oldPassword, String newPassword) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AuthenticationException("User not found"));

        // Verify old password using BCrypt
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new AuthenticationException("Current password is incorrect");
        }

        // Update with new encrypted password
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    /**
     * Check if a password meets security requirements
     * 
     * @param password the password to check
     * @return true if password is secure, false otherwise
     */
    public boolean isPasswordSecure(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasUpper = password.chars().anyMatch(Character::isUpperCase);
        boolean hasLower = password.chars().anyMatch(Character::isLowerCase);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        boolean hasSpecial = password.chars().anyMatch(ch -> "!@#$%^&*()_+-=[]{}|;:,.<>?".indexOf(ch) >= 0);

        return hasUpper && hasLower && hasDigit && hasSpecial;
    }
}