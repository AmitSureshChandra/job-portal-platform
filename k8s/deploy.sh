#!/bin/bash

echo "Deploying Job Portal to Kubernetes..."

# Deploy infrastructure first
echo "1. Deploying infrastructure..."
kubectl apply -f infrastructure/

# Wait for infrastructure to be ready
echo "2. Waiting for infrastructure to be ready..."
kubectl wait --for=condition=available --timeout=300s deployment/postgres
kubectl wait --for=condition=available --timeout=300s deployment/elasticsearch
kubectl wait --for=condition=available --timeout=300s deployment/zookeeper
kubectl wait --for=condition=available --timeout=300s deployment/kafka

# Deploy core services
echo "3. Deploying core services..."
kubectl apply -f services/eureka-server.yaml
kubectl apply -f services/config-server.yaml

# Wait for core services
echo "4. Waiting for core services..."
kubectl wait --for=condition=available --timeout=300s deployment/eureka-server
kubectl wait --for=condition=available --timeout=300s deployment/config-server

# Deploy microservices
echo "5. Deploying microservices..."
kubectl apply -f services/microservices.yaml
kubectl apply -f services/advanced-services.yaml

# Deploy API Gateway last
echo "6. Deploying API Gateway..."
kubectl apply -f services/api-gateway.yaml

echo "7. Deployment complete!"
echo "Getting service status..."
kubectl get pods
kubectl get services

echo ""
echo "Access the application via API Gateway:"
kubectl get service api-gateway-service