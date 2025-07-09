# Docker Configuration for Spring Boot Auth API

This document provides comprehensive instructions for running the Spring Boot Auth API using Docker.

## 🚀 Quick Start

### Prerequisites
- Docker and Docker Compose installed
- Git (for cloning the repository)

### Start the Application
```bash
./docker-start.sh
```

### Stop the Application
```bash
./docker-stop.sh
```

## 📁 Docker Files Overview

| File | Purpose |
|------|---------|
| `Dockerfile` | Multi-stage build configuration for the Spring Boot application |
| `docker-compose.yml` | Main orchestration file with PostgreSQL and Redis |
| `docker-compose.override.yml` | Development overrides with pgAdmin and Mailhog |
| `.dockerignore` | Excludes unnecessary files from Docker build context |
| `.env` | Environment variables configuration |
| `init-db.sql` | PostgreSQL database initialization script |
| `docker-start.sh` | Convenient script to start all services |
| `docker-stop.sh` | Script to stop all services |
| `docker-clean.sh` | Script to clean up everything including volumes |

## 🏗️ Architecture

The Docker setup includes:

### Core Services
- **auth-api**: Spring Boot application (Port 8080)
- **postgres**: PostgreSQL 15 database (Port 5432)
- **redis**: Redis for caching/sessions (Port 6379)

### Development Services (via override)
- **pgadmin**: Database management UI (Port 5050)
- **mailhog**: Email testing tool (Port 8025)

## 🌐 Service URLs

After starting with `./docker-start.sh`:

| Service | URL | Credentials |
|---------|-----|-------------|
| Auth API | http://localhost:8080 | - |
| API Documentation | http://localhost:8080/swagger-ui.html | - |
| Health Check | http://localhost:8080/actuator/health | - |
| pgAdmin | http://localhost:5050 | admin@authapi.com / admin123 |
| Mailhog UI | http://localhost:8025 | - |

## ⚙️ Configuration

### Environment Variables

Edit the `.env` file to customize configuration:

```bash
# Database
POSTGRES_DB=authdb
POSTGRES_USER=authuser
POSTGRES_PASSWORD=authpass123

# JWT Configuration
JWT_SECRET=your-secret-key-here
JWT_EXPIRATION=86400000

# Logging
LOG_LEVEL=INFO
```

### Spring Profiles

- **docker**: Used in containerized environment
- **dev**: Additional development settings

## 🛠️ Development

### Hot Reload (Optional)
Uncomment the volume mapping in `docker-compose.override.yml`:
```yaml
volumes:
  - ./src:/app/src:ro
```

### View Logs
```bash
# All services
docker-compose logs -f

# Specific service
docker-compose logs -f auth-api
```

### Execute Commands in Container
```bash
# Access application container
docker-compose exec auth-api bash

# Access database
docker-compose exec postgres psql -U authuser -d authdb
```

## 🗄️ Database Management

### Using pgAdmin
1. Access pgAdmin at http://localhost:5050
2. Login with: admin@authapi.com / admin123
3. Add server connection:
   - Name: Auth API DB
   - Host: postgres
   - Port: 5432
   - Database: authdb
   - Username: authuser
   - Password: authpass123

### Direct PostgreSQL Access
```bash
docker-compose exec postgres psql -U authuser -d authdb
```

## 🔧 Troubleshooting

### Common Issues

1. **Port Conflicts**
   - Ensure ports 8080, 5432, 6379, 5050, 8025 are available
   - Modify ports in `docker-compose.yml` if needed

2. **Memory Issues**
   - Increase Docker memory limit (recommended: 4GB+)

3. **Database Connection Issues**
   - Check if PostgreSQL container is healthy: `docker-compose ps`
   - Verify connection settings in `.env` file

4. **Application Won't Start**
   - Check logs: `docker-compose logs auth-api`
   - Ensure all dependencies are built: `./docker-clean.sh` then `./docker-start.sh`

### Rebuilding

```bash
# Rebuild specific service
docker-compose build auth-api

# Rebuild everything
docker-compose build --no-cache

# Clean rebuild
./docker-clean.sh
./docker-start.sh
```

## 🧪 Testing

### API Testing
```bash
# Health check
curl http://localhost:8080/actuator/health

# API endpoints (after authentication)
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"test","email":"test@example.com","password":"password123"}'
```

## 🚀 Production Deployment

### Security Considerations

1. **Change Default Passwords**
   - Update `.env` with secure passwords
   - Generate new JWT secret key

2. **Use External Database**
   - Configure external PostgreSQL instance
   - Update connection settings

3. **Environment-Specific Configuration**
   - Create production-specific environment files
   - Use secrets management for sensitive data

### Docker Compose Production Override
```yaml
# docker-compose.prod.yml
version: '3.8'
services:
  auth-api:
    restart: always
    environment:
      SPRING_PROFILES_ACTIVE: docker,prod
    # Remove development tools
  
  # Remove development services
  pgadmin: null
  mailhog: null
```

Run with:
```bash
docker-compose -f docker-compose.yml -f docker-compose.prod.yml up -d
```

## 📊 Monitoring

### Health Checks
All services include health checks with automatic restart policies.

### Logs
```bash
# Follow all logs
docker-compose logs -f

# Follow specific service
docker-compose logs -f auth-api

# Show last N lines
docker-compose logs --tail=100 auth-api
```

## 🔄 Updates

### Application Updates
```bash
# Pull latest code
git pull

# Rebuild and restart
docker-compose up --build -d auth-api
```

### Dependency Updates
```bash
# Update base images
docker-compose pull

# Rebuild with latest dependencies
docker-compose build --no-cache
```

---

For more information, see the main [README.md](README.md) file.