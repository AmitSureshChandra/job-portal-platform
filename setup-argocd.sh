#!/bin/bash

echo "🚀 Setting up ArgoCD with MicroK8s"

# Check if MicroK8s is running
if ! microk8s status --wait-ready --timeout=10 > /dev/null 2>&1; then
    echo "❌ MicroK8s is not running. Please start MicroK8s first."
    exit 1
fi

# Install ArgoCD
echo "📦 Installing ArgoCD..."
kubectl create namespace argocd
kubectl apply -n argocd -f https://raw.githubusercontent.com/argoproj/argo-cd/stable/manifests/install.yaml

# Wait for ArgoCD to be ready
echo "⏳ Waiting for ArgoCD to be ready..."
kubectl wait --for=condition=available --timeout=300s deployment/argocd-server -n argocd

# Setup NodePort access for ArgoCD UI
echo "🌐 Setting up ArgoCD UI access..."
kubectl patch svc argocd-server -n argocd -p '{"spec": {"type": "NodePort", "ports": [{"port": 443, "targetPort": 8080, "nodePort": 30443}]}}'

# Get admin password
echo "🔑 Getting ArgoCD admin password..."
ARGOCD_PASSWORD=$(kubectl -n argocd get secret argocd-initial-admin-secret -o jsonpath="{.data.password}" | base64 -d)

# Create ArgoCD application for Job Portal
echo "📱 Creating ArgoCD application..."
kubectl apply -f argocd-apps/jobportal-app.yaml

# Wait a moment for application to be created
sleep 10

echo ""
echo "✅ ArgoCD setup complete!"
echo ""
echo "🌐 ArgoCD UI: https://localhost:30443"
echo "👤 Username: admin"
echo "🔑 Password: $ARGOCD_PASSWORD"
echo ""
echo "📱 Application Status:"
kubectl get applications -n argocd
echo ""
echo "💡 Next Steps:"
echo "1. Update the Git repository URL in argocd-apps/jobportal-app.yaml"
echo "2. Push your code to Git repository"
echo "3. Access ArgoCD UI and sync the application"
echo ""
echo "🔧 Useful Commands:"
echo "kubectl get applications -n argocd"
echo "kubectl describe application jobportal -n argocd"
echo "kubectl logs -f deployment/argocd-server -n argocd"