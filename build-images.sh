#!/bin/bash

# Build all microservices
echo "Building all microservices..."
./gradlew build

# Build Docker images for each service
services=("eureka-server" "config-server" "api-gateway" "user-service" "company-service" "job-service" "application-service" "search-service" "notification-service")

for service in "${services[@]}"; do
    echo "Building Docker image for $service..."
    cd $service
    cp ../Dockerfile .
    docker build -t jobportal/$service:latest .
    rm Dockerfile
    cd ..
done

echo "All Docker images built successfully!"
echo "Images created:"
docker images | grep jobportal