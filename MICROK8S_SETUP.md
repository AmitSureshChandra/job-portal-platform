# MicroK8s Setup Guide

## 1. Install MicroK8s

### Ubuntu/Debian
```bash
# Install via snap
sudo snap install microk8s --classic

# Add user to microk8s group
sudo usermod -a -G microk8s $USER
newgrp microk8s

# Verify installation
microk8s status --wait-ready
```

### Other Linux Distributions
```bash
# Install snapd first
# CentOS/RHEL/Fedora
sudo dnf install snapd
sudo systemctl enable --now snapd.socket

# Then install microk8s
sudo snap install microk8s --classic
sudo usermod -a -G microk8s $USER
newgrp microk8s
```

## 2. Enable Required Add-ons

```bash
# Enable essential add-ons
microk8s enable dns
microk8s enable storage
microk8s enable dashboard
microk8s enable registry

# Check status
microk8s status
```

## 3. Configure kubectl

```bash
# Create .kube directory
mkdir -p ~/.kube

# Copy microk8s config to kubectl
microk8s config > ~/.kube/config

# Or use microk8s kubectl directly
alias kubectl='microk8s kubectl'

# Verify
kubectl get nodes
kubectl get pods --all-namespaces
```

## 4. Deploy Job Portal

### Build and Import Images
```bash
# Build Docker images
./build-images.sh

# Import images to MicroK8s
services=("eureka-server" "config-server" "api-gateway" "user-service" "company-service" "job-service" "application-service" "search-service" "notification-service")

for service in "${services[@]}"; do
    echo "Importing $service..."
    docker save jobportal/$service:latest | microk8s ctr image import -
done

# Verify images imported
microk8s ctr images list | grep jobportal
```

### Deploy Services
```bash
# Deploy to MicroK8s
cd k8s
kubectl apply -f infrastructure/
kubectl apply -f services/

# Check deployment status
kubectl get pods --watch
```

## 5. Access Application

### Option 1: NodePort Service
```bash
# Edit api-gateway service to use NodePort
kubectl patch service api-gateway-service -p '{"spec":{"type":"NodePort","ports":[{"port":8080,"targetPort":8080,"nodePort":30080}]}}'

# Access via node IP
curl http://localhost:30080/api/users/health
```

### Option 2: Port Forward
```bash
# Port forward to local machine
kubectl port-forward service/api-gateway-service 8080:8080

# Access application
curl http://localhost:8080/api/users/health
```

### Option 3: Ingress (Advanced)
```bash
# Enable ingress
microk8s enable ingress

# Create ingress resource
kubectl apply -f - <<EOF
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: jobportal-ingress
spec:
  rules:
  - host: jobportal.local
    http:
      paths:
      - path: /
        pathType: Prefix
        backend:
          service:
            name: api-gateway-service
            port:
              number: 8080
EOF

# Add to /etc/hosts
echo "127.0.0.1 jobportal.local" | sudo tee -a /etc/hosts

# Access via http://jobportal.local
```

## 6. Useful Commands

### Check Status
```bash
# MicroK8s status
microk8s status

# Cluster info
kubectl cluster-info

# All pods
kubectl get pods --all-namespaces

# Services
kubectl get services
```

### Logs and Debugging
```bash
# View pod logs
kubectl logs -f deployment/api-gateway

# Describe pod
kubectl describe pod <pod-name>

# Shell into pod
kubectl exec -it <pod-name> -- /bin/bash
```

### Scale Services
```bash
# Scale deployment
kubectl scale deployment user-service --replicas=3

# Check scaling
kubectl get deployments
```

### Dashboard Access
```bash
# Get dashboard token
token=$(microk8s kubectl -n kube-system get secret | grep default-token | cut -d " " -f1)
microk8s kubectl -n kube-system describe secret $token

# Access dashboard
microk8s dashboard-proxy
# Open: https://127.0.0.1:10443
```

## 7. Cleanup

### Remove Deployments
```bash
kubectl delete -f k8s/services/
kubectl delete -f k8s/infrastructure/
```

### Stop MicroK8s
```bash
microk8s stop
```

### Uninstall MicroK8s
```bash
microk8s reset
sudo snap remove microk8s
```

## Quick Setup Script

```bash
#!/bin/bash
# Install and setup MicroK8s
sudo snap install microk8s --classic
sudo usermod -a -G microk8s $USER
newgrp microk8s

# Enable add-ons
microk8s enable dns storage dashboard registry

# Configure kubectl
mkdir -p ~/.kube
microk8s config > ~/.kube/config

# Build and deploy
./build-images.sh

# Import images
services=("eureka-server" "config-server" "api-gateway" "user-service" "company-service" "job-service" "application-service" "search-service" "notification-service")
for service in "${services[@]}"; do
    docker save jobportal/$service:latest | microk8s ctr image import -
done

# Deploy
cd k8s && kubectl apply -f infrastructure/ && kubectl apply -f services/

# Setup NodePort access
kubectl patch service api-gateway-service -p '{"spec":{"type":"NodePort","ports":[{"port":8080,"targetPort":8080,"nodePort":30080}]}}'

echo "✅ MicroK8s setup complete!"
echo "🌐 Access: http://localhost:30080"
```