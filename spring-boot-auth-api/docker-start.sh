#!/bin/bash

# Spring Boot Auth API - Docker Start Script

echo "🚀 Starting Spring Boot Auth API with Docker..."

# Check if .env file exists
if [ ! -f .env ]; then
    echo "⚠️  .env file not found. Using default configuration."
fi

# Build and start services
echo "📦 Building and starting services..."
docker-compose --env-file .env up --build -d

# Wait for services to be healthy
echo "⏳ Waiting for services to be ready..."
sleep 10

# Check service status
echo "🔍 Checking service status..."
docker-compose ps

# Show application logs
echo "📋 Application logs:"
docker-compose logs auth-api

echo "✅ Services are starting up!"
echo ""
echo "🌐 Application will be available at:"
echo "   - Auth API: http://localhost:8080"
echo "   - API Documentation: http://localhost:8080/swagger-ui.html"
echo "   - Health Check: http://localhost:8080/actuator/health"
echo "   - pgAdmin: http://localhost:5050 (admin@authapi.com / admin123)"
echo "   - Mailhog UI: http://localhost:8025"
echo ""
echo "📊 Database Connection:"
echo "   - Host: localhost"
echo "   - Port: 5432"
echo "   - Database: authdb"
echo "   - Username: authuser"
echo "   - Password: authpass123"
echo ""
echo "🛑 To stop services: ./docker-stop.sh"
echo "📝 To view logs: docker-compose logs -f"