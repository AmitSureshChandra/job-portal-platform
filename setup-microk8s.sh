#!/bin/bash

echo "🚀 Setting up MicroK8s for Job Portal"

# Check if running on Linux
if [[ "$OSTYPE" != "linux-gnu"* ]]; then
    echo "❌ MicroK8s requires Linux. Use Minikube for other OS."
    exit 1
fi

# Install MicroK8s
echo "📦 Installing MicroK8s..."
sudo snap install microk8s --classic

# Add user to group
echo "👤 Adding user to microk8s group..."
sudo usermod -a -G microk8s $USER

# Apply group changes
echo "🔄 Applying group changes..."
newgrp microk8s

# Wait for MicroK8s to be ready
echo "⏳ Waiting for MicroK8s to be ready..."
microk8s status --wait-ready

# Enable required add-ons
echo "🔧 Enabling add-ons..."
microk8s enable dns
microk8s enable storage
microk8s enable dashboard
microk8s enable registry

# Configure kubectl
echo "⚙️ Configuring kubectl..."
mkdir -p ~/.kube
microk8s config > ~/.kube/config

# Create kubectl alias
echo "alias kubectl='microk8s kubectl'" >> ~/.bashrc

# Verify installation
echo "✅ Verifying installation..."
microk8s kubectl get nodes

# Build Docker images
echo "🔨 Building Docker images..."
./build-images.sh

# Import images to MicroK8s
echo "📦 Importing images to MicroK8s..."
services=("eureka-server" "config-server" "api-gateway" "user-service" "company-service" "job-service" "application-service" "search-service" "notification-service")

for service in "${services[@]}"; do
    echo "Importing jobportal/$service:latest..."
    docker save jobportal/$service:latest | microk8s ctr image import -
done

# Deploy infrastructure
echo "🏗️ Deploying infrastructure..."
cd k8s
microk8s kubectl apply -f infrastructure/

# Wait for infrastructure
echo "⏳ Waiting for infrastructure..."
sleep 30

# Deploy services
echo "🚀 Deploying services..."
microk8s kubectl apply -f services/

# Setup NodePort for external access
echo "🌐 Setting up external access..."
microk8s kubectl patch service api-gateway-service -p '{"spec":{"type":"NodePort","ports":[{"port":8080,"targetPort":8080,"nodePort":30080}]}}'

# Wait for deployment
echo "⏳ Waiting for services to start..."
sleep 60

# Show status
echo ""
echo "🎉 MicroK8s setup complete!"
echo ""
echo "📊 Cluster Status:"
microk8s kubectl get nodes
echo ""
echo "🔍 Pod Status:"
microk8s kubectl get pods
echo ""
echo "🌐 Service Status:"
microk8s kubectl get services
echo ""
echo "🚀 Access Job Portal:"
echo "   URL: http://localhost:30080"
echo "   Test: curl http://localhost:30080/api/users/health"
echo ""
echo "📊 Dashboard:"
echo "   Run: microk8s dashboard-proxy"
echo ""
echo "💡 Useful Commands:"
echo "   kubectl get pods"
echo "   kubectl logs -f deployment/api-gateway"
echo "   kubectl scale deployment user-service --replicas=3"