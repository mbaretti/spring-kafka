package com.authapi.demo;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * BCrypt Password Encryption Demo
 * 
 * Demonstrates how the BCrypt algorithm works for password encryption
 * in the Spring Boot Auth API. Run this with: ./gradlew runBCryptDemo
 */
public class BCryptDemo {
    
    public static void main(String[] args) {
        System.out.println("=".repeat(80));
        System.out.println("🔐 BCrypt Password Encryption Demonstration");
        System.out.println("=".repeat(80));
        
        // Create BCrypt password encoder (same as used in the API)
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
        
        // Test passwords
        String[] testPasswords = {
            "mySecurePassword123",
            "anotherPassword456", 
            "SimplePass",
            "Complex@Pass#2023!"
        };
        
        System.out.println("\n📊 BCrypt Password Encryption Results:");
        System.out.println("-".repeat(80));
        
        for (String password : testPasswords) {
            System.out.printf("📝 Original Password: %s%n", password);
            
            // Encrypt the password (each call generates a unique salt)
            String encrypted1 = passwordEncoder.encode(password);
            String encrypted2 = passwordEncoder.encode(password);
            
            System.out.printf("🔒 Encrypted Hash 1: %s%n", encrypted1);
            System.out.printf("🔒 Encrypted Hash 2: %s%n", encrypted2);
            
            // Verify passwords
            boolean match1 = passwordEncoder.matches(password, encrypted1);
            boolean match2 = passwordEncoder.matches(password, encrypted2);
            boolean wrongMatch = passwordEncoder.matches("wrongPassword", encrypted1);
            
            System.out.printf("✅ Password matches hash 1: %s%n", match1);
            System.out.printf("✅ Password matches hash 2: %s%n", match2);
            System.out.printf("❌ Wrong password matches: %s%n", wrongMatch);
            
            // Show that each hash is unique (different salt)
            System.out.printf("🔄 Hashes are different: %s%n", !encrypted1.equals(encrypted2));
            
            System.out.println("-".repeat(80));
        }
        
        System.out.println("\n🔐 BCrypt Security Features:");
        System.out.println("• Each password generates a unique salt automatically");
        System.out.println("• Same password produces different hashes each time");
        System.out.println("• Computationally expensive to prevent brute force attacks");
        System.out.println("• Uses cost factor of 12 (4096 iterations)");
        System.out.println("• Salt and hash are combined in a single string");
        System.out.println("• Resistant to rainbow table attacks");
        
        System.out.println("\n📱 API Usage Example:");
        System.out.println("POST /api/auth/register");
        System.out.println("{\n  \"username\": \"user123\",");
        System.out.println("  \"email\": \"user@example.com\",");
        System.out.println("  \"password\": \"mySecurePassword123\"\n}");
        
        System.out.println("\n🎯 The password will be automatically encrypted using BCrypt");
        System.out.println("   before being stored in the database!");
        
        System.out.println("\n" + "=".repeat(80));
    }
}