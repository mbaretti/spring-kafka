package com.authapi.dto;

import com.authapi.entity.User;

/**
 * DTO for authentication responses
 * 
 * Contains the JWT token and user information returned after successful
 * authentication (login or registration).
 */
public class AuthResponse {

    private String token;
    private String type = "Bearer";
    private UserInfo user;

    // Constructors
    public AuthResponse() {}

    public AuthResponse(String token, UserInfo user) {
        this.token = token;
        this.user = user;
    }

    // Static factory method for convenience
    public static AuthResponse of(String token, User user) {
        UserInfo userInfo = new UserInfo(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getRole().name()
        );
        return new AuthResponse(token, userInfo);
    }

    // Getters and Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    /**
     * Nested class for user information in the response
     * 
     * Contains only the necessary user information to be sent to the client,
     * excluding sensitive data like passwords.
     */
    public static class UserInfo {
        private Long id;
        private String username;
        private String email;
        private String role;

        public UserInfo() {}

        public UserInfo(Long id, String username, String email, String role) {
            this.id = id;
            this.username = username;
            this.email = email;
            this.role = role;
        }

        // Getters and Setters
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }
    }
}