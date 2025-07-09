# Flyway Database Migration Guide

This document provides comprehensive instructions for using Flyway database migrations in the Spring Boot Auth API.

## 📁 Migration Structure

```
src/main/resources/db/migration/
├── V1__Create_users_table.sql          # Initial schema
├── V2__Insert_default_users.sql        # Default user data
└── V{n}__{Description}.sql             # Future migrations
```

## 🏗️ Migration Files Overview

### V1__Create_users_table.sql
- Creates the `users` table with all necessary columns
- Adds indexes for performance optimization
- Sets up constraints and validation rules
- Includes comprehensive documentation via comments

### V2__Insert_default_users.sql
- Inserts default admin and test users
- Creates database triggers for automatic timestamp updates
- Provides sample data for development and testing

## ⚙️ Flyway Configuration

### Application Properties
```properties
# Flyway Configuration
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
spring.flyway.baseline-on-migrate=true
spring.flyway.validate-on-migrate=true
spring.flyway.out-of-order=false
spring.flyway.clean-disabled=true
```

### Key Settings Explained
- **baseline-on-migrate**: Automatically baseline existing databases
- **validate-on-migrate**: Validate migrations before applying
- **clean-disabled**: Prevent accidental data loss in production
- **out-of-order**: Enforce strict migration ordering

## 🚀 Usage

### Automatic Migration
Migrations run automatically when the application starts:
```bash
./docker-start.sh  # Migrations run automatically
```

### Manual Migration Commands
```bash
# Build the application (triggers Flyway)
./gradlew build

# Run only Flyway migrations
./gradlew flywayMigrate

# Get migration status
./gradlew flywayInfo

# Validate migrations
./gradlew flywayValidate
```

### Docker Environment
In Docker, Flyway runs automatically during application startup:
1. PostgreSQL container starts and initializes
2. Application connects to database
3. Flyway checks for pending migrations
4. Migrations are applied in order
5. Application starts normally

## 📝 Creating New Migrations

### Naming Convention
```
V{VERSION}__{DESCRIPTION}.sql

Examples:
V3__Add_user_profile_table.sql
V4__Add_email_verification.sql
V5__Update_user_roles.sql
```

### Migration Template
```sql
-- ===============================
-- Flyway Migration V{N}: {Description}
-- Description: {Detailed description of changes}
-- Author: {Your Name}
-- Date: {Date}
-- ===============================

-- Your SQL commands here
CREATE TABLE example (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Add indexes
CREATE INDEX idx_example_name ON example(name);

-- Add comments
COMMENT ON TABLE example IS 'Example table description';
```

### Best Practices
1. **Use descriptive names**: Clear purpose in migration name
2. **Add comments**: Document what and why
3. **Test migrations**: Validate on development environment first
4. **Keep atomic**: One logical change per migration
5. **No rollbacks**: Design migrations to be forward-only

## 🔧 Password Hash Generation

### Using the Generator
```bash
./gradlew generatePasswordHashes
```

### Manual Hash Generation
```java
// In your migration script or test
BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
String hash = encoder.encode("password123");
```

### Example Migration with Hashes
```sql
-- Insert new user with BCrypt password
INSERT INTO users (username, email, password, role) 
VALUES (
    'newuser',
    'new@example.com',
    '$2a$12$your_generated_hash_here',
    'USER'
);
```

## 🗄️ Database Schema

### Current Schema (V1)
```sql
users table:
├── id (BIGSERIAL, PRIMARY KEY)
├── username (VARCHAR(50), UNIQUE, NOT NULL)
├── email (VARCHAR(100), UNIQUE, NOT NULL)
├── password (VARCHAR(255), NOT NULL)
├── role (VARCHAR(20), DEFAULT 'USER')
├── account_non_expired (BOOLEAN, DEFAULT true)
├── account_non_locked (BOOLEAN, DEFAULT true)
├── credentials_non_expired (BOOLEAN, DEFAULT true)
├── enabled (BOOLEAN, DEFAULT true)
├── created_at (TIMESTAMP WITH TIME ZONE)
└── updated_at (TIMESTAMP WITH TIME ZONE)
```

### Default Users (V2)
| Username | Email | Password | Role | Status |
|----------|-------|----------|------|--------|
| admin | admin@authapi.com | admin123 | ADMIN | Enabled |
| testuser | test@authapi.com | user123 | USER | Enabled |
| demouser | demo@authapi.com | demo123 | USER | Enabled |
| disabled | disabled@authapi.com | disabled123 | USER | Disabled |

## 🧪 Testing Migrations

### Development Testing
1. Start with clean database: `./docker-clean.sh`
2. Start application: `./docker-start.sh`
3. Verify migrations applied: Check logs
4. Test with sample data

### Migration Validation
```bash
# Check migration status
docker-compose exec auth-api bash
java -jar app.jar --spring.profiles.active=docker --flyway.info=true
```

### Database Verification
```bash
# Connect to database
docker-compose exec postgres psql -U authuser -d authdb

# Check Flyway schema history
SELECT * FROM flyway_schema_history ORDER BY installed_rank;

# Verify table creation
\dt

# Check user data
SELECT username, email, role, enabled FROM users;
```

## 🚨 Troubleshooting

### Common Issues

1. **Migration Checksum Mismatch**
   - **Cause**: Modified existing migration file
   - **Solution**: Never modify applied migrations; create new ones

2. **Out of Order Migration**
   - **Cause**: Created migration with older version number
   - **Solution**: Use next sequential version number

3. **SQL Syntax Error**
   - **Cause**: Invalid SQL in migration file
   - **Solution**: Test SQL manually before adding to migration

4. **Permission Denied**
   - **Cause**: Database user lacks necessary permissions
   - **Solution**: Check init-db.sql for proper grants

### Recovery Commands
```bash
# Reset development database completely
./docker-clean.sh
./docker-start.sh

# Check Flyway status
./gradlew flywayInfo

# Validate all migrations
./gradlew flywayValidate
```

### Production Considerations
- **Never use flyway:clean** in production
- **Test migrations** on staging environment first
- **Backup database** before major migrations
- **Monitor migration performance** for large datasets

## 📊 Monitoring

### Flyway Schema History
```sql
-- View applied migrations
SELECT 
    installed_rank,
    version,
    description,
    execution_time,
    installed_on,
    success
FROM flyway_schema_history 
ORDER BY installed_rank;
```

### Migration Logs
```bash
# View application logs during startup
docker-compose logs -f auth-api | grep -i flyway
```

## 🔄 Advanced Usage

### Conditional Migrations
```sql
-- Only create if not exists
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'example') THEN
        CREATE TABLE example (
            id BIGSERIAL PRIMARY KEY,
            name VARCHAR(100)
        );
    END IF;
END $$;
```

### Data Migration
```sql
-- Migrate data between tables
INSERT INTO new_users (username, email, role)
SELECT username, email, 'USER' 
FROM legacy_users 
WHERE active = true;
```

### Performance Optimization
```sql
-- Create indexes concurrently (PostgreSQL)
CREATE INDEX CONCURRENTLY idx_users_email_domain 
ON users (split_part(email, '@', 2));
```

---

For more information, see:
- [Flyway Documentation](https://flywaydb.org/documentation/)
- [Spring Boot Flyway Integration](https://docs.spring.io/spring-boot/docs/current/reference/html/howto.html#howto.data-initialization.migration-tool.flyway)
- [DOCKER_README.md](DOCKER_README.md) for Docker setup