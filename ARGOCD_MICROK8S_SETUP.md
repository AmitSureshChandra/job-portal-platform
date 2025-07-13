# ArgoCD with MicroK8s Setup Guide

## 1. Install ArgoCD on MicroK8s

### Install ArgoCD
```bash
# Create argocd namespace
kubectl create namespace argocd

# Install ArgoCD
kubectl apply -n argocd -f https://raw.githubusercontent.com/argoproj/argo-cd/stable/manifests/install.yaml

# Wait for ArgoCD to be ready
kubectl wait --for=condition=available --timeout=300s deployment/argocd-server -n argocd
```

### Access ArgoCD UI
```bash
# Option 1: Port Forward
kubectl port-forward svc/argocd-server -n argocd 8080:443

# Option 2: NodePort (Recommended for MicroK8s)
kubectl patch svc argocd-server -n argocd -p '{"spec": {"type": "NodePort", "ports": [{"port": 443, "targetPort": 8080, "nodePort": 30443}]}}'

# Access via: https://localhost:30443
```

### Get ArgoCD Admin Password
```bash
# Get initial admin password
kubectl -n argocd get secret argocd-initial-admin-secret -o jsonpath="{.data.password}" | base64 -d && echo

# Login credentials:
# Username: admin
# Password: <output from above command>
```

## 2. Prepare Git Repository

### Create ArgoCD Application Manifests
```bash
mkdir -p argocd-apps
```

### Create Application for Job Portal
```yaml
# argocd-apps/jobportal-app.yaml
apiVersion: argoproj.io/v1alpha1
kind: Application
metadata:
  name: jobportal
  namespace: argocd
spec:
  project: default
  source:
    repoURL: https://github.com/your-username/learning-microservice.git
    targetRevision: HEAD
    path: k8s
  destination:
    server: https://kubernetes.default.svc
    namespace: default
  syncPolicy:
    automated:
      prune: true
      selfHeal: true
    syncOptions:
    - CreateNamespace=true
```

### Push to Git Repository
```bash
# Initialize git repo (if not already)
git init
git add .
git commit -m "Add Kubernetes manifests and ArgoCD config"

# Push to GitHub/GitLab
git remote add origin https://github.com/your-username/learning-microservice.git
git push -u origin main
```

## 3. Configure ArgoCD Application

### Method 1: Via ArgoCD UI
1. Open ArgoCD UI: `https://localhost:30443`
2. Login with admin credentials
3. Click "NEW APP"
4. Fill in details:
   - **Application Name**: jobportal
   - **Project**: default
   - **Repository URL**: `https://github.com/your-username/learning-microservice.git`
   - **Path**: `k8s`
   - **Cluster URL**: `https://kubernetes.default.svc`
   - **Namespace**: `default`
5. Click "CREATE"

### Method 2: Via kubectl
```bash
# Apply the application manifest
kubectl apply -f argocd-apps/jobportal-app.yaml

# Check application status
kubectl get applications -n argocd
```

## 4. ArgoCD CLI Setup (Optional)

### Install ArgoCD CLI
```bash
# Linux
curl -sSL -o argocd-linux-amd64 https://github.com/argoproj/argo-cd/releases/latest/download/argocd-linux-amd64
sudo install -m 555 argocd-linux-amd64 /usr/local/bin/argocd

# macOS
brew install argocd
```

### Login via CLI
```bash
# Login to ArgoCD
argocd login localhost:30443 --username admin --password <admin-password> --insecure

# List applications
argocd app list

# Sync application
argocd app sync jobportal
```

## 5. GitOps Workflow

### Directory Structure
```
learning-microservice/
├── k8s/
│   ├── infrastructure/
│   │   ├── postgres.yaml
│   │   ├── elasticsearch.yaml
│   │   └── kafka.yaml
│   └── services/
│       ├── eureka-server.yaml
│       ├── config-server.yaml
│       ├── api-gateway.yaml
│       ├── microservices.yaml
│       └── advanced-services.yaml
├── argocd-apps/
│   └── jobportal-app.yaml
└── README.md
```

### Update Application
```bash
# Make changes to Kubernetes manifests
vim k8s/services/api-gateway.yaml

# Commit and push changes
git add .
git commit -m "Update API Gateway configuration"
git push

# ArgoCD will automatically detect and sync changes
```

## 6. Advanced ArgoCD Configuration

### Create ArgoCD Project
```yaml
# argocd-apps/jobportal-project.yaml
apiVersion: argoproj.io/v1alpha1
kind: AppProject
metadata:
  name: jobportal
  namespace: argocd
spec:
  description: Job Portal Microservices Project
  sourceRepos:
  - 'https://github.com/your-username/learning-microservice.git'
  destinations:
  - namespace: default
    server: https://kubernetes.default.svc
  - namespace: jobportal
    server: https://kubernetes.default.svc
  clusterResourceWhitelist:
  - group: ''
    kind: Namespace
  namespaceResourceWhitelist:
  - group: ''
    kind: '*'
  - group: 'apps'
    kind: '*'
  - group: 'extensions'
    kind: '*'
```

### Multi-Environment Setup
```yaml
# argocd-apps/jobportal-dev.yaml
apiVersion: argoproj.io/v1alpha1
kind: Application
metadata:
  name: jobportal-dev
  namespace: argocd
spec:
  project: jobportal
  source:
    repoURL: https://github.com/your-username/learning-microservice.git
    targetRevision: develop
    path: k8s/overlays/dev
  destination:
    server: https://kubernetes.default.svc
    namespace: jobportal-dev
  syncPolicy:
    automated:
      prune: true
      selfHeal: true
```

## 7. Monitoring and Troubleshooting

### Check ArgoCD Status
```bash
# Check ArgoCD pods
kubectl get pods -n argocd

# Check application status
kubectl get applications -n argocd

# View application details
kubectl describe application jobportal -n argocd
```

### View Sync Status
```bash
# Via CLI
argocd app get jobportal

# Via kubectl
kubectl get application jobportal -n argocd -o yaml
```

### Manual Sync
```bash
# Force sync via CLI
argocd app sync jobportal --force

# Via kubectl
kubectl patch application jobportal -n argocd --type merge -p '{"operation":{"sync":{"revision":"HEAD"}}}'
```

## 8. Complete Setup Script

```bash
#!/bin/bash

echo "🚀 Setting up ArgoCD with MicroK8s"

# Install ArgoCD
kubectl create namespace argocd
kubectl apply -n argocd -f https://raw.githubusercontent.com/argoproj/argo-cd/stable/manifests/install.yaml

# Wait for ArgoCD
echo "⏳ Waiting for ArgoCD to be ready..."
kubectl wait --for=condition=available --timeout=300s deployment/argocd-server -n argocd

# Setup NodePort access
kubectl patch svc argocd-server -n argocd -p '{"spec": {"type": "NodePort", "ports": [{"port": 443, "targetPort": 8080, "nodePort": 30443}]}}'

# Get admin password
echo "🔑 ArgoCD Admin Password:"
kubectl -n argocd get secret argocd-initial-admin-secret -o jsonpath="{.data.password}" | base64 -d && echo

# Create application
kubectl apply -f argocd-apps/jobportal-app.yaml

echo ""
echo "✅ ArgoCD setup complete!"
echo "🌐 ArgoCD UI: https://localhost:30443"
echo "👤 Username: admin"
echo "🔑 Password: (shown above)"
echo ""
echo "📱 Application Status:"
kubectl get applications -n argocd
```

## Key Benefits

- **GitOps**: Infrastructure as Code
- **Automated Deployment**: Push to Git → Auto Deploy
- **Rollback**: Easy rollback to previous versions
- **Multi-Environment**: Dev, Staging, Production
- **Security**: RBAC and policy enforcement
- **Monitoring**: Visual deployment status

Your Job Portal will now be managed via GitOps with ArgoCD! 🎉