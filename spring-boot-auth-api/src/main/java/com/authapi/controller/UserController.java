package com.authapi.controller;

import com.authapi.dto.ApiResponse;
import com.authapi.entity.User;
import com.authapi.exception.AuthenticationException;
import com.authapi.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * User Management REST Controller
 * 
 * Provides protected endpoints for authenticated users including:
 * - User profile management
 * - Password change with BCrypt encryption
 * - User information retrieval
 */
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

    private final AuthService authService;

    @Autowired
    public UserController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Get Current User Profile
     * 
     * Returns the profile information of the currently authenticated user.
     * Requires authentication via JWT token.
     * 
     * @return ResponseEntity with user profile information
     */
    @GetMapping("/profile")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<User.UserProfile>> getCurrentUserProfile() {
        
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = (User) authentication.getPrincipal();
            
            // Create profile DTO without sensitive information
            User.UserProfile profile = new User.UserProfile(
                    currentUser.getId(),
                    currentUser.getUsername(),
                    currentUser.getEmail(),
                    currentUser.getRole().name(),
                    currentUser.getCreatedAt(),
                    currentUser.getUpdatedAt(),
                    currentUser.isEnabled()
            );
            
            return ResponseEntity.ok()
                    .body(ApiResponse.success("Profile retrieved successfully", profile));
                    
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve profile: " + e.getMessage()));
        }
    }

    /**
     * Change Password Endpoint
     * 
     * Allows authenticated users to change their password.
     * Uses BCrypt encryption for the new password.
     * 
     * @param passwordRequest the password change request
     * @return ResponseEntity with success or error message
     */
    @PostMapping("/change-password")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<String>> changePassword(
            @RequestBody Map<String, String> passwordRequest) {
        
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = (User) authentication.getPrincipal();
            
            String oldPassword = passwordRequest.get("oldPassword");
            String newPassword = passwordRequest.get("newPassword");
            
            if (oldPassword == null || newPassword == null) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Both oldPassword and newPassword are required"));
            }
            
            // Validate new password strength
            if (!authService.isPasswordSecure(newPassword)) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("New password must be at least 8 characters long and contain uppercase, lowercase, digit, and special character"));
            }
            
            // Change password with BCrypt encryption
            authService.changePassword(currentUser.getUsername(), oldPassword, newPassword);
            
            return ResponseEntity.ok()
                    .body(ApiResponse.success("Password changed successfully"));
                    
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(e.getMessage()));
                    
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to change password: " + e.getMessage()));
        }
    }

    /**
     * Get User Statistics (Demo protected endpoint)
     * 
     * Example of a protected endpoint that returns user statistics.
     * Demonstrates role-based access control.
     * 
     * @return ResponseEntity with user statistics
     */
    @GetMapping("/stats")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getUserStats() {
        
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = (User) authentication.getPrincipal();
            
            Map<String, Object> stats = Map.of(
                    "userId", currentUser.getId(),
                    "username", currentUser.getUsername(),
                    "role", currentUser.getRole().name(),
                    "accountAge", java.time.Duration.between(currentUser.getCreatedAt(), java.time.LocalDateTime.now()).toDays(),
                    "isEnabled", currentUser.isEnabled(),
                    "lastLogin", java.time.LocalDateTime.now() // This would come from a login tracking system
            );
            
            return ResponseEntity.ok()
                    .body(ApiResponse.success("User statistics retrieved successfully", stats));
                    
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve user statistics: " + e.getMessage()));
        }
    }

    /**
     * Admin Only Endpoint (Demo)
     * 
     * Example endpoint that requires ADMIN role.
     * Demonstrates method-level security with @PreAuthorize.
     * 
     * @return ResponseEntity with admin information
     */
    @GetMapping("/admin/info")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> getAdminInfo() {
        
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = (User) authentication.getPrincipal();
            
            String message = String.format("Welcome Admin %s! You have access to administrative functions.", 
                    currentUser.getUsername());
            
            return ResponseEntity.ok()
                    .body(ApiResponse.success(message, "ADMIN_ACCESS_GRANTED"));
                    
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve admin information: " + e.getMessage()));
        }
    }
}