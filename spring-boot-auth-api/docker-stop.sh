#!/bin/bash

# Spring Boot Auth API - Docker Stop Script

echo "🛑 Stopping Spring Boot Auth API Docker services..."

# Stop and remove containers
docker-compose down

echo "✅ All services stopped!"
echo ""
echo "💾 Data volumes are preserved:"
echo "   - postgres_data"
echo "   - redis_data"
echo "   - pgadmin_data"
echo ""
echo "🗑️  To remove everything including data: ./docker-clean.sh"