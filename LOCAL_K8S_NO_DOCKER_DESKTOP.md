# Local Kubernetes Without Docker Desktop

## Option 1: Minikube (Recommended)

### Install Minikube
```bash
# Linux
curl -LO https://storage.googleapis.com/minikube/releases/latest/minikube-linux-amd64
sudo install minikube-linux-amd64 /usr/local/bin/minikube

# macOS (without Docker Desktop)
brew install minikube

# Windows
winget install minikube
```

### Install kubectl
```bash
# Linux
curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl"
sudo install -o root -g root -m 0755 kubectl /usr/local/bin/kubectl

# macOS
brew install kubectl

# Windows
winget install kubectl
```

### Start Minikube
```bash
# Start with VirtualBox driver (no Docker needed)
minikube start --driver=virtualbox --memory=8192 --cpus=4

# Or with KVM2 (Linux only)
minikube start --driver=kvm2 --memory=8192 --cpus=4

# Verify
minikube status
kubectl get nodes
```

## Option 2: Kind (Kubernetes in Docker)

### Install Docker (without Desktop)
```bash
# Linux - Install Docker Engine only
curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh get-docker.sh
sudo usermod -aG docker $USER
newgrp docker

# Start Docker service
sudo systemctl start docker
sudo systemctl enable docker
```

### Install Kind
```bash
# Linux
curl -Lo ./kind https://kind.sigs.k8s.io/dl/v0.20.0/kind-linux-amd64
chmod +x ./kind
sudo mv ./kind /usr/local/bin/kind

# macOS
brew install kind
```

### Create Cluster
```bash
kind create cluster --name jobportal --config=- <<EOF
kind: Cluster
apiVersion: kind.x-k8s.io/v1alpha4
nodes:
- role: control-plane
  extraPortMappings:
  - containerPort: 30080
    hostPort: 8080
    protocol: TCP
EOF
```

## Option 3: MicroK8s (Ubuntu/Linux)

### Install MicroK8s
```bash
# Ubuntu/Debian
sudo snap install microk8s --classic

# Add user to microk8s group
sudo usermod -a -G microk8s $USER
newgrp microk8s

# Enable required addons
microk8s enable dns dashboard storage
```

### Configure kubectl
```bash
microk8s kubectl config view --raw > $HOME/.kube/config
kubectl get nodes
```

## Option 4: K3s (Lightweight Kubernetes)

### Install K3s
```bash
# Install K3s server
curl -sfL https://get.k3s.io | sh -

# Configure kubectl
sudo cp /etc/rancher/k3s/k3s.yaml ~/.kube/config
sudo chown $USER ~/.kube/config
```

## Deploy Job Portal

### For Minikube
```bash
# Build images in minikube's Docker environment
eval $(minikube docker-env)
./build-images.sh

# Deploy
cd k8s && ./deploy.sh

# Access application
minikube service api-gateway-service --url
```

### For Kind
```bash
# Build images locally
./build-images.sh

# Load images to kind cluster
services=("eureka-server" "config-server" "api-gateway" "user-service" "company-service" "job-service" "application-service" "search-service" "notification-service")
for service in "${services[@]}"; do
    kind load docker-image jobportal/$service:latest --name jobportal
done

# Deploy
cd k8s && ./deploy.sh

# Access via port forward
kubectl port-forward service/api-gateway-service 8080:8080
```

### For MicroK8s
```bash
# Build images
./build-images.sh

# Import images to MicroK8s
services=("eureka-server" "config-server" "api-gateway" "user-service" "company-service" "job-service" "application-service" "search-service" "notification-service")
for service in "${services[@]}"; do
    docker save jobportal/$service:latest | microk8s ctr image import -
done

# Deploy
cd k8s && ./deploy.sh

# Access application
kubectl get service api-gateway-service
```

## Comparison

| Option | Pros | Cons | Best For |
|--------|------|------|----------|
| **Minikube** | Easy setup, VM isolation | Requires VM driver | Development |
| **Kind** | Fast, lightweight | Needs Docker Engine | CI/CD, Testing |
| **MicroK8s** | Production-like, snap packages | Ubuntu/Linux only | Local production |
| **K3s** | Very lightweight, fast | Less features | Edge, IoT, Simple setups |

## Recommended: Minikube Setup

```bash
# 1. Install Minikube with VirtualBox
minikube start --driver=virtualbox --memory=8192 --cpus=4

# 2. Build and deploy
eval $(minikube docker-env)
./build-images.sh
cd k8s && ./deploy.sh

# 3. Access application
minikube service api-gateway-service --url

# 4. Test API
curl $(minikube service api-gateway-service --url)/api/users/health
```

This gives you a full Kubernetes environment without Docker Desktop!