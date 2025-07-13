# Kubernetes Deployment Guide

## Prerequisites
- Kubernetes cluster (minikube, Docker Desktop, or cloud provider)
- kubectl configured
- Docker installed

## Deployment Steps

### 1. Build Docker Images
```bash
# Build all microservices and create Docker images
./build-images.sh
```

### 2. Deploy to Kubernetes
```bash
# Deploy all services to Kubernetes
cd k8s
./deploy.sh
```

### 3. Access the Application
```bash
# Get API Gateway external IP
kubectl get service api-gateway-service

# If using minikube
minikube service api-gateway-service --url
```

## Architecture in Kubernetes

### Infrastructure Layer
- **PostgreSQL**: Database for all services
- **Elasticsearch**: Search engine for job search
- **Kafka + Zookeeper**: Message queue for notifications

### Service Layer
- **Eureka Server**: Service discovery
- **Config Server**: Centralized configuration
- **API Gateway**: Single entry point (LoadBalancer)

### Microservices (2 replicas each)
- **user-service**: Port 8081
- **company-service**: Port 8082  
- **job-service**: Port 8083
- **application-service**: Port 8084
- **search-service**: Port 8085
- **notification-service**: Port 8086

## Useful Commands

```bash
# Check pod status
kubectl get pods

# Check services
kubectl get services

# View logs
kubectl logs -f deployment/api-gateway

# Scale services
kubectl scale deployment user-service --replicas=3

# Delete deployment
kubectl delete -f .
```

## Access URLs
- API Gateway: `http://<EXTERNAL-IP>:8080`
- All APIs available through gateway:
  - `/api/users/**`
  - `/api/companies/**`
  - `/api/jobs/**`
  - `/api/applications/**`
  - `/api/search/**`
  - `/api/notifications/**`