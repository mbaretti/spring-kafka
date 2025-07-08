package com.authapi.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for BCrypt password encryption functionality
 * 
 * These tests verify that password encryption and verification work correctly
 * in the authentication system.
 */
@DisplayName("BCrypt Password Encoder Tests")
class BCryptPasswordEncoderTest {

    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        // Use the same BCrypt configuration as the application (cost factor 12)
        passwordEncoder = new BCryptPasswordEncoder(12);
    }

    @Test
    @DisplayName("Should encrypt password with unique salt")
    void shouldEncryptPasswordWithUniqueSalt() {
        String plainPassword = "SecurePassword123!";
        
        String encrypted1 = passwordEncoder.encode(plainPassword);
        String encrypted2 = passwordEncoder.encode(plainPassword);
        
        assertNotNull(encrypted1);
        assertNotNull(encrypted2);
        assertNotEquals(encrypted1, encrypted2, "Each encryption should produce unique hash due to different salts");
        
        // Verify BCrypt format: $2a$12$...
        assertTrue(encrypted1.startsWith("$2a$12$"), "Should use BCrypt with cost factor 12");
        assertTrue(encrypted2.startsWith("$2a$12$"), "Should use BCrypt with cost factor 12");
    }

    @Test
    @DisplayName("Should verify correct password")
    void shouldVerifyCorrectPassword() {
        String plainPassword = "MySecurePassword456!";
        String encryptedPassword = passwordEncoder.encode(plainPassword);
        
        boolean matches = passwordEncoder.matches(plainPassword, encryptedPassword);
        
        assertTrue(matches, "Correct password should match encrypted hash");
    }

    @Test
    @DisplayName("Should reject incorrect password")
    void shouldRejectIncorrectPassword() {
        String plainPassword = "CorrectPassword789!";
        String wrongPassword = "WrongPassword123!";
        String encryptedPassword = passwordEncoder.encode(plainPassword);
        
        boolean matches = passwordEncoder.matches(wrongPassword, encryptedPassword);
        
        assertFalse(matches, "Incorrect password should not match encrypted hash");
    }

    @Test
    @DisplayName("Should handle empty and null passwords securely")
    void shouldHandleEmptyAndNullPasswordsSecurely() {
        String normalPassword = "NormalPassword123!";
        String encryptedPassword = passwordEncoder.encode(normalPassword);
        
        // Test empty password
        assertFalse(passwordEncoder.matches("", encryptedPassword), 
                   "Empty password should not match any hash");
        
        // Test null password (should throw exception or return false)
        assertThrows(IllegalArgumentException.class, 
                    () -> passwordEncoder.matches(null, encryptedPassword),
                    "Null password should throw exception");
    }

    @Test
    @DisplayName("Should work with complex passwords containing special characters")
    void shouldWorkWithComplexPasswords() {
        String complexPassword = "P@ssw0rd!#$%^&*()_+-=[]{}|;:,.<>?`~";
        
        String encryptedPassword = passwordEncoder.encode(complexPassword);
        
        assertNotNull(encryptedPassword);
        assertTrue(passwordEncoder.matches(complexPassword, encryptedPassword),
                  "Complex password with special characters should work correctly");
    }

    @Test
    @DisplayName("Should work with long passwords")
    void shouldWorkWithLongPasswords() {
        String longPassword = "ThisIsAVeryLongPasswordThatContainsLotsOfCharactersAndShouldStillWorkCorrectlyWithBCryptEncryption123!@#";
        
        String encryptedPassword = passwordEncoder.encode(longPassword);
        
        assertNotNull(encryptedPassword);
        assertTrue(passwordEncoder.matches(longPassword, encryptedPassword),
                  "Long passwords should be handled correctly");
    }

    @Test
    @DisplayName("Should be consistent across multiple verifications")
    void shouldBeConsistentAcrossMultipleVerifications() {
        String password = "ConsistencyTestPassword123!";
        String encryptedPassword = passwordEncoder.encode(password);
        
        // Verify the same password multiple times
        for (int i = 0; i < 10; i++) {
            assertTrue(passwordEncoder.matches(password, encryptedPassword),
                      "Password verification should be consistent across multiple calls");
        }
    }

    @Test
    @DisplayName("Should use secure cost factor")
    void shouldUseSecureCostFactor() {
        String password = "TestPassword123!";
        String encryptedPassword = passwordEncoder.encode(password);
        
        // Verify that the hash uses cost factor 12 (which is secure)
        String[] parts = encryptedPassword.split("\\$");
        assertEquals("2a", parts[1], "Should use BCrypt algorithm version 2a");
        assertEquals("12", parts[2], "Should use cost factor 12 for security");
    }

    @Test
    @DisplayName("Should perform reasonably quickly for cost factor 12")
    void shouldPerformReasonablyQuickly() {
        String password = "PerformanceTestPassword123!";
        
        long startTime = System.currentTimeMillis();
        String encryptedPassword = passwordEncoder.encode(password);
        long encryptionTime = System.currentTimeMillis() - startTime;
        
        startTime = System.currentTimeMillis();
        passwordEncoder.matches(password, encryptedPassword);
        long verificationTime = System.currentTimeMillis() - startTime;
        
        // With cost factor 12, encryption should take a reasonable amount of time
        // (typically 100ms to 1000ms depending on hardware)
        assertTrue(encryptionTime > 10, "Encryption should take some time for security");
        assertTrue(encryptionTime < 5000, "Encryption should not take too long");
        assertTrue(verificationTime < 5000, "Verification should be reasonably fast");
        
        assertNotNull(encryptedPassword);
        assertTrue(passwordEncoder.matches(password, encryptedPassword));
    }
}