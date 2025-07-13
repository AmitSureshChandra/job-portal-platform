# Kubernetes Basics

## What is Kubernetes?
Kubernetes (K8s) is a container orchestration platform that manages containerized applications across multiple machines.

## Core Concepts

### 1. Pod
**Smallest deployable unit** - contains one or more containers
```yaml
apiVersion: v1
kind: Pod
metadata:
  name: my-app
spec:
  containers:
  - name: app
    image: nginx
    ports:
    - containerPort: 80
```

### 2. Deployment
**Manages Pods** - ensures desired number of replicas are running
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: nginx-deployment
spec:
  replicas: 3
  selector:
    matchLabels:
      app: nginx
  template:
    metadata:
      labels:
        app: nginx
    spec:
      containers:
      - name: nginx
        image: nginx:1.20
        ports:
        - containerPort: 80
```

### 3. Service
**Network access to Pods** - provides stable IP and DNS
```yaml
apiVersion: v1
kind: Service
metadata:
  name: nginx-service
spec:
  selector:
    app: nginx
  ports:
  - port: 80
    targetPort: 80
  type: ClusterIP  # Internal only
```

### 4. ConfigMap
**Configuration data** - stores non-sensitive config
```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: app-config
data:
  database_url: "postgres://localhost:5432/mydb"
  debug: "true"
```

### 5. Secret
**Sensitive data** - stores passwords, tokens
```yaml
apiVersion: v1
kind: Secret
metadata:
  name: app-secret
type: Opaque
data:
  password: cGFzc3dvcmQ=  # base64 encoded
```

## Basic Commands

### Cluster Info
```bash
kubectl cluster-info          # Cluster information
kubectl get nodes            # List nodes
kubectl get namespaces       # List namespaces
```

### Working with Pods
```bash
kubectl get pods             # List pods
kubectl describe pod <name>  # Pod details
kubectl logs <pod-name>      # View logs
kubectl exec -it <pod> bash  # Shell into pod
kubectl delete pod <name>    # Delete pod
```

### Working with Deployments
```bash
kubectl get deployments     # List deployments
kubectl create deployment nginx --image=nginx
kubectl scale deployment nginx --replicas=5
kubectl rollout status deployment/nginx
kubectl delete deployment nginx
```

### Working with Services
```bash
kubectl get services         # List services
kubectl expose deployment nginx --port=80 --type=NodePort
kubectl port-forward service/nginx 8080:80
```

### Apply YAML Files
```bash
kubectl apply -f app.yaml    # Create/update resources
kubectl delete -f app.yaml   # Delete resources
kubectl get -f app.yaml      # Get resources from file
```

## Hands-On Example

### 1. Create a Simple App
```yaml
# app.yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: hello-app
spec:
  replicas: 1
  selector:
    matchLabels:
      app: hello
  template:
    metadata:
      labels:
        app: hello
    spec:
      containers:
      - name: hello
        image: gcr.io/google-samples/hello-app:1.0
        ports:
        - containerPort: 8080
---
apiVersion: v1
kind: Service
metadata:
  name: hello-service
spec:
  selector:
    app: hello
  ports:
  - port: 80
    targetPort: 8080
  type: NodePort
```

### 2. Deploy and Test
```bash
# Deploy
kubectl apply -f app.yaml

# Check status
kubectl get pods
kubectl get services

# Access app
kubectl port-forward service/hello-service 8080:80
curl http://localhost:8080

# Scale up
kubectl scale deployment hello-app --replicas=5

# Clean up
kubectl delete -f app.yaml
```

## Service Types

### ClusterIP (Default)
- **Internal only** - accessible within cluster
- Use for: databases, internal APIs

### NodePort
- **External access** via node IP:port
- Port range: 30000-32767
- Use for: development, testing

### LoadBalancer
- **Cloud load balancer** - gets external IP
- Use for: production web apps

### Ingress
- **HTTP/HTTPS routing** - domain-based routing
- Use for: multiple services, SSL termination

## Namespaces
**Logical separation** of resources
```bash
kubectl create namespace dev
kubectl get pods -n dev
kubectl apply -f app.yaml -n dev
```

## Labels and Selectors
**Organize and select resources**
```yaml
metadata:
  labels:
    app: frontend
    version: v1
    environment: prod
```

```bash
kubectl get pods -l app=frontend
kubectl get pods -l environment=prod,version=v1
```

## Resource Management
```yaml
spec:
  containers:
  - name: app
    image: nginx
    resources:
      requests:
        memory: "64Mi"
        cpu: "250m"
      limits:
        memory: "128Mi"
        cpu: "500m"
```

## Health Checks
```yaml
spec:
  containers:
  - name: app
    image: nginx
    livenessProbe:
      httpGet:
        path: /health
        port: 8080
      initialDelaySeconds: 30
      periodSeconds: 10
    readinessProbe:
      httpGet:
        path: /ready
        port: 8080
      initialDelaySeconds: 5
      periodSeconds: 5
```

## Practice Exercises

### Exercise 1: Deploy Nginx
```bash
# Create deployment
kubectl create deployment nginx --image=nginx

# Expose service
kubectl expose deployment nginx --port=80 --type=NodePort

# Check and access
kubectl get services
kubectl port-forward service/nginx 8080:80
```

### Exercise 2: Use ConfigMap
```bash
# Create configmap
kubectl create configmap app-config --from-literal=ENV=production

# Use in deployment
kubectl create deployment app --image=nginx --dry-run=client -o yaml > app.yaml
# Edit app.yaml to add configmap
kubectl apply -f app.yaml
```

### Exercise 3: Scale and Update
```bash
# Scale deployment
kubectl scale deployment nginx --replicas=3

# Update image
kubectl set image deployment/nginx nginx=nginx:1.21

# Check rollout
kubectl rollout status deployment/nginx

# Rollback if needed
kubectl rollout undo deployment/nginx
```

## Key Takeaways

1. **Pod** = Running container(s)
2. **Deployment** = Manages pods
3. **Service** = Network access
4. **ConfigMap/Secret** = Configuration
5. **kubectl** = Command-line tool
6. **YAML** = Resource definitions
7. **Labels** = Organization
8. **Namespaces** = Isolation

## Next Steps
- Practice with simple apps
- Learn about Ingress
- Understand persistent volumes
- Explore monitoring tools
- Study security best practices

Start with simple deployments and gradually add complexity! 🚀