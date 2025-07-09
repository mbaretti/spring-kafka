-- ===============================
-- Database Initialization Script for Docker PostgreSQL
-- ===============================
-- This script runs automatically when the PostgreSQL container starts
-- Flyway will handle all schema migrations and data insertion

-- Create extensions if needed
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Set timezone
SET timezone = 'UTC';

-- Grant all necessary permissions to the application user
GRANT ALL PRIVILEGES ON DATABASE authdb TO authuser;

-- Grant schema permissions (for Flyway)
GRANT ALL ON SCHEMA public TO authuser;

-- Grant sequence permissions (for auto-increment)
GRANT ALL ON ALL SEQUENCES IN SCHEMA public TO authuser;

-- Grant table permissions (for future tables created by Flyway)
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO authuser;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO authuser;

-- Note: All table creation and data insertion is now handled by Flyway migrations
-- See src/main/resources/db/migration/ for migration scripts