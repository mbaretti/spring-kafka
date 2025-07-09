-- ===============================
-- Flyway Migration V1: Create users table
-- Description: Initial schema creation for user authentication system
-- Author: Auth API Team
-- Date: 2024
-- ===============================

-- Create users table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'USER',
    account_non_expired BOOLEAN NOT NULL DEFAULT true,
    account_non_locked BOOLEAN NOT NULL DEFAULT true,
    credentials_non_expired BOOLEAN NOT NULL DEFAULT true,
    enabled BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for performance
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_role ON users(role);
CREATE INDEX idx_users_enabled ON users(enabled);
CREATE INDEX idx_users_created_at ON users(created_at);

-- Add check constraints
ALTER TABLE users ADD CONSTRAINT chk_users_role 
    CHECK (role IN ('USER', 'ADMIN'));

ALTER TABLE users ADD CONSTRAINT chk_users_username_length 
    CHECK (length(username) >= 3 AND length(username) <= 50);

ALTER TABLE users ADD CONSTRAINT chk_users_email_length 
    CHECK (length(email) <= 100);

-- Add comments for documentation
COMMENT ON TABLE users IS 'User accounts for authentication and authorization';
COMMENT ON COLUMN users.id IS 'Primary key, auto-generated';
COMMENT ON COLUMN users.username IS 'Unique username for login, 3-50 characters';
COMMENT ON COLUMN users.email IS 'Unique email address, max 100 characters';
COMMENT ON COLUMN users.password IS 'BCrypt hashed password';
COMMENT ON COLUMN users.role IS 'User role: USER or ADMIN';
COMMENT ON COLUMN users.account_non_expired IS 'Spring Security: account expiry status';
COMMENT ON COLUMN users.account_non_locked IS 'Spring Security: account lock status';
COMMENT ON COLUMN users.credentials_non_expired IS 'Spring Security: credentials expiry status';
COMMENT ON COLUMN users.enabled IS 'Spring Security: account enabled status';
COMMENT ON COLUMN users.created_at IS 'Timestamp when user was created';
COMMENT ON COLUMN users.updated_at IS 'Timestamp when user was last updated';