-- ===============================
-- Flyway Migration V2: Insert default users
-- Description: Insert default admin user and sample test users for development
-- Author: Auth API Team
-- Date: 2024
-- ===============================

-- Insert default admin user
-- Password: admin123 (BCrypt hash with strength 12)
INSERT INTO users (username, email, password, role, created_at, updated_at) 
VALUES (
    'admin',
    'admin@authapi.com',
    '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewYQ8UH.HyB7XGi',
    'ADMIN',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

-- Insert sample regular user for testing
-- Password: user123 (BCrypt hash with strength 12)
INSERT INTO users (username, email, password, role, created_at, updated_at) 
VALUES (
    'testuser',
    'test@authapi.com',
    '$2a$12$k8fTJfCE8Jq8QqXjRJ7V6eKzLp8QqXjRJ7V6eKzLp8QqXjRJ7V6e',
    'USER',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

-- Insert another sample user for testing
-- Password: demo123 (BCrypt hash with strength 12)
INSERT INTO users (username, email, password, role, enabled, created_at, updated_at) 
VALUES (
    'demouser',
    'demo@authapi.com',
    '$2a$12$m9gUKfDE9Mr9RrYkSK8W7fNaOq9RrYkSK8W7fNaOq9RrYkSK8W7f',
    'USER',
    true,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

-- Insert a disabled user for testing account status
-- Password: disabled123 (BCrypt hash with strength 12)
INSERT INTO users (username, email, password, role, enabled, created_at, updated_at) 
VALUES (
    'disabled',
    'disabled@authapi.com',
    '$2a$12$n0hVLgEF0Ns0SsZlTL9X8gOcPr0SsZlTL9X8gOcPr0SsZlTL9X8g',
    'USER',
    false,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

-- Create function to update the updated_at timestamp automatically
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Create trigger to automatically update the updated_at column
CREATE TRIGGER users_updated_at_trigger
    BEFORE UPDATE ON users
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();