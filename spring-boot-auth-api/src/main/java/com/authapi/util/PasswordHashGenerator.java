package com.authapi.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Utility class to generate BCrypt password hashes for use in Flyway migration scripts
 * 
 * This class can be run standalone to generate password hashes that can be used
 * in database migration scripts for default users.
 * 
 * Usage: ./gradlew generatePasswordHashes
 */
public class PasswordHashGenerator {

    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("BCrypt Password Hash Generator for Flyway Migrations");
        System.out.println("=".repeat(60));
        
        // Generate hashes for default passwords
        String[] passwords = {
            "admin123",    // Admin user password
            "user123",     // Test user password
            "demo123",     // Demo user password
            "disabled123"  // Disabled user password
        };
        
        String[] usernames = {
            "admin",
            "testuser", 
            "demouser",
            "disabled"
        };
        
        for (int i = 0; i < passwords.length; i++) {
            String hash = passwordEncoder.encode(passwords[i]);
            System.out.printf("Username: %-12s | Password: %-12s | Hash: %s%n", 
                            usernames[i], passwords[i], hash);
        }
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("Copy these hashes to your Flyway migration scripts");
        System.out.println("=".repeat(60));
        
        // Generate additional example hashes
        System.out.println("\nAdditional example hashes:");
        String[] examplePasswords = {"password123", "test123", "secure456"};
        for (String password : examplePasswords) {
            String hash = passwordEncoder.encode(password);
            System.out.printf("Password: %-12s | Hash: %s%n", password, hash);
        }
        
        System.out.println("\nNote: Each run generates different hashes due to salt randomization.");
        System.out.println("This is normal BCrypt behavior for security.");
    }
    
    /**
     * Generate a BCrypt hash for a given password
     * @param password The plain text password
     * @return BCrypt hash string
     */
    public static String generateHash(String password) {
        return passwordEncoder.encode(password);
    }
    
    /**
     * Verify a password against a hash
     * @param password The plain text password
     * @param hash The BCrypt hash
     * @return true if password matches hash
     */
    public static boolean verifyPassword(String password, String hash) {
        return passwordEncoder.matches(password, hash);
    }
}