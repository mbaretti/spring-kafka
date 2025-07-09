-- ===============================
-- Database Initialization Script
-- ===============================

-- Create database if not exists (handled by POSTGRES_DB env var)
-- This script runs automatically when the container starts

-- Create extensions if needed
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Set timezone
SET timezone = 'UTC';

-- Create schemas if needed
-- CREATE SCHEMA IF NOT EXISTS auth;

-- Grant permissions
GRANT ALL PRIVILEGES ON DATABASE authdb TO authuser;

-- Create any additional database objects here
-- Tables will be created automatically by Hibernate with ddl-auto=update

-- Insert any initial data if needed
-- This will run only once when the database is first created

-- Example: Create a default admin user (uncomment if needed)
-- Note: This should be handled by the application startup logic instead
/*
INSERT INTO users (id, username, email, password, role, created_at, updated_at) 
VALUES (
    uuid_generate_v4(),
    'admin',
    'admin@example.com',
    '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewYQ8UH..INC3wE.', -- BCrypt hash for 'admin123'
    'ADMIN',
    NOW(),
    NOW()
) ON CONFLICT (username) DO NOTHING;
*/