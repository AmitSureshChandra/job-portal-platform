#!/bin/bash

echo "🚀 Job Portal Local Kubernetes Setup"

# Check if kubectl is installed
if ! command -v kubectl &> /dev/null; then
    echo "❌ kubectl not found. Please install kubectl first."
    exit 1
fi

# Check if Docker is running
if ! docker info &> /dev/null; then
    echo "❌ Docker not running. Please start Docker first."
    exit 1
fi

# Detect Kubernetes environment
if kubectl config current-context | grep -q "docker-desktop"; then
    echo "✅ Using Docker Desktop Kubernetes"
    K8S_ENV="docker-desktop"
elif kubectl config current-context | grep -q "minikube"; then
    echo "✅ Using Minikube"
    K8S_ENV="minikube"
elif kubectl config current-context | grep -q "kind"; then
    echo "✅ Using Kind"
    K8S_ENV="kind"
else
    echo "⚠️  Unknown Kubernetes environment"
    K8S_ENV="unknown"
fi

# Build Docker images
echo "🔨 Building Docker images..."
cd ..
./build-images.sh

# Load images for minikube
if [ "$K8S_ENV" = "minikube" ]; then
    echo "📦 Loading images to minikube..."
    eval $(minikube docker-env)
    ./build-images.sh
fi

# Load images for kind
if [ "$K8S_ENV" = "kind" ]; then
    echo "📦 Loading images to kind..."
    services=("eureka-server" "config-server" "api-gateway" "user-service" "company-service" "job-service" "application-service" "search-service" "notification-service")
    for service in "${services[@]}"; do
        kind load docker-image jobportal/$service:latest
    done
fi

# Deploy to Kubernetes
echo "🚀 Deploying to Kubernetes..."
cd k8s
./deploy.sh

# Wait for deployment
echo "⏳ Waiting for services to be ready..."
sleep 30

# Show access information
echo ""
echo "🎉 Deployment Complete!"
echo "📊 Service Status:"
kubectl get pods
echo ""
echo "🌐 Access Information:"

if [ "$K8S_ENV" = "docker-desktop" ]; then
    echo "API Gateway: http://localhost:8080"
elif [ "$K8S_ENV" = "minikube" ]; then
    echo "Run: minikube service api-gateway-service --url"
elif [ "$K8S_ENV" = "kind" ]; then
    echo "Run: kubectl port-forward service/api-gateway-service 8080:8080"
    echo "Then access: http://localhost:8080"
fi

echo ""
echo "🧪 Test API:"
echo "curl http://localhost:8080/api/users/health"