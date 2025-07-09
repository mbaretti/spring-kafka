#!/bin/bash

# Spring Boot Auth API - Docker Clean Script

echo "🗑️  Cleaning up Spring Boot Auth API Docker environment..."
echo "⚠️  This will remove all containers, images, and data volumes!"

read -p "Are you sure? (y/N): " -n 1 -r
echo
if [[ $REPLY =~ ^[Yy]$ ]]; then
    # Stop and remove containers, networks, images, and volumes
    docker-compose down -v --rmi all --remove-orphans
    
    # Remove any dangling images
    docker image prune -f
    
    # Remove any dangling volumes
    docker volume prune -f
    
    echo "✅ Environment cleaned up!"
    echo "🔄 Next run will rebuild everything from scratch."
else
    echo "❌ Operation cancelled."
fi