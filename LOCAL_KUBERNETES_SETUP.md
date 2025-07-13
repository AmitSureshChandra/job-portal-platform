# Local Kubernetes Setup Guide

## Option 1: Docker Desktop (Recommended)

### Install Docker Desktop
```bash
# Download from: https://www.docker.com/products/docker-desktop
# Enable Kubernetes in Docker Desktop settings
```

### Enable Kubernetes
1. Open Docker Desktop
2. Go to Settings → Kubernetes
3. Check "Enable Kubernetes"
4. Click "Apply & Restart"

### Verify Installation
```bash
kubectl version --client
kubectl cluster-info
```

## Option 2: Minikube

### Install Minikube
```bash
# macOS
brew install minikube

# Linux
curl -LO https://storage.googleapis.com/minikube/releases/latest/minikube-linux-amd64
sudo install minikube-linux-amd64 /usr/local/bin/minikube

# Windows
choco install minikube
```

### Start Minikube
```bash
minikube start --driver=docker --memory=8192 --cpus=4
minikube status
```

### Configure kubectl
```bash
kubectl config use-context minikube
kubectl get nodes
```

## Option 3: Kind (Kubernetes in Docker)

### Install Kind
```bash
# macOS
brew install kind

# Linux
curl -Lo ./kind https://kind.sigs.k8s.io/dl/v0.20.0/kind-linux-amd64
chmod +x ./kind
sudo mv ./kind /usr/local/bin/kind
```

### Create Cluster
```bash
kind create cluster --name jobportal
kubectl cluster-info --context kind-jobportal
```

## Deploy Job Portal

### 1. Build Images
```bash
# Build all microservices
./build-images.sh

# For minikube - load images
if using minikube:
eval $(minikube docker-env)
./build-images.sh
```

### 2. Deploy to Kubernetes
```bash
cd k8s
./deploy.sh
```

### 3. Access Application

#### Docker Desktop
```bash
kubectl get service api-gateway-service
# Access via http://localhost:8080
```

#### Minikube
```bash
minikube service api-gateway-service --url
# Use the provided URL
```

#### Kind
```bash
kubectl port-forward service/api-gateway-service 8080:8080
# Access via http://localhost:8080
```

## Useful Commands

### Check Status
```bash
kubectl get pods
kubectl get services
kubectl get deployments
```

### View Logs
```bash
kubectl logs -f deployment/api-gateway
kubectl logs -f deployment/user-service
```

### Scale Services
```bash
kubectl scale deployment user-service --replicas=3
```

### Delete Everything
```bash
kubectl delete -f k8s/services/
kubectl delete -f k8s/infrastructure/
```

### Restart Cluster
```bash
# Docker Desktop: Restart from UI
# Minikube:
minikube stop
minikube start

# Kind:
kind delete cluster --name jobportal
kind create cluster --name jobportal
```

## Troubleshooting

### Images Not Found
```bash
# For minikube
eval $(minikube docker-env)
./build-images.sh

# For kind
kind load docker-image jobportal/api-gateway:latest --name jobportal
```

### Services Not Starting
```bash
kubectl describe pod <pod-name>
kubectl logs <pod-name>
```

### Port Access Issues
```bash
# Port forward specific service
kubectl port-forward service/api-gateway-service 8080:8080
```

## Quick Start (Docker Desktop)
```bash
# 1. Enable Kubernetes in Docker Desktop
# 2. Build and deploy
./build-images.sh
cd k8s && ./deploy.sh

# 3. Wait for pods to be ready
kubectl get pods --watch

# 4. Access application
curl http://localhost:8080/api/users/health
```