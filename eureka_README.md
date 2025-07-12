# Eureka Server - Service Discovery

## Overview
Eureka Server acts as a **Service Registry** - the "phone book" of your microservices architecture.

## Core Functions

### 1. Service Registration
- Services register themselves with Eureka on startup
- Each service provides: name, IP, port, health check URL
- Services send heartbeats every 30 seconds to stay "alive"

### 2. Service Discovery
- Services query Eureka to find other services
- No need for hardcoded IP addresses or ports
- Dynamic service location resolution

### 3. Load Balancing
- Eureka provides multiple instances of same service
- Client-side load balancing through service lists
- Automatic failover when instances go down

## Architecture Flow

```
┌─────────────────┐    ┌──────────────────┐
│   Eureka Server │◄───┤  user-service    │ (Registers)
│   (Port 8761)   │    │  (Port 8081)     │
└─────────────────┘    └──────────────────┘
         ▲                       
         │               ┌──────────────────┐
         └───────────────┤  job-service     │ (Discovers user-service)
                         │  (Port 8083)     │
                         └──────────────────┘
```

## Code Examples

### Without Eureka (Hard-coded)
```java
// job-service calling user-service
String userServiceUrl = "http://192.168.1.100:8081/api/users/1";
RestTemplate restTemplate = new RestTemplate();
User user = restTemplate.getForObject(userServiceUrl, User.class);
```

### With Eureka (Dynamic Discovery)
```java
// job-service discovers user-service dynamically
@FeignClient(name = "user-service")
public interface UserServiceClient {
    @GetMapping("/api/users/{id}")
    User getUser(@PathVariable Long id);
}
```

## Benefits

### Dynamic Scaling
- Start multiple user-service instances (8081, 8082, 8083)
- Eureka automatically distributes traffic
- No configuration changes needed

### Fault Tolerance
- If user-service:8081 crashes, Eureka removes it
- Traffic automatically routes to healthy instances
- Self-healing architecture

### Environment Flexibility
- Same code works in dev, staging, production
- Services find each other regardless of deployment location
- No environment-specific configuration

## Configuration

### Eureka Server (application.yml)
```yaml
server:
  port: 8761

eureka:
  client:
    register-with-eureka: false
    fetch-registry: false
  server:
    enable-self-preservation: false
```

### Eureka Client (application.yml)
```yaml
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
  instance:
    prefer-ip-address: true
```

## Running Eureka Server

```bash
# Start Eureka Server
./gradlew :eureka-server:bootRun

# Start client services
./gradlew :user-service:bootRun
./gradlew :job-service:bootRun
```

## Eureka Dashboard

**URL:** http://localhost:8761

**Features:**
- View all registered services
- Monitor instance health status
- Check service metadata
- View registration timestamps
- Monitor heartbeat status

## Service Registration Process

1. **Service Startup** → Service registers with Eureka
2. **Heartbeat** → Service sends heartbeat every 30s
3. **Discovery** → Other services query Eureka for service locations
4. **Load Balance** → Client chooses from available instances
5. **Health Check** → Unhealthy instances removed automatically

## Key Annotations

```java
// Eureka Server
@EnableEurekaServer

// Eureka Client
@EnableDiscoveryClient
// or
@EnableEurekaClient
```

## Troubleshooting

### Service Not Appearing in Eureka
- Check `eureka.client.service-url.defaultZone`
- Verify network connectivity to Eureka server
- Check application name in `spring.application.name`

### Service Discovery Failing
- Ensure `@EnableDiscoveryClient` annotation
- Verify Eureka client dependencies
- Check service name spelling in `@FeignClient`

### Multiple Instances Not Load Balancing
- Confirm different ports for each instance
- Check `eureka.instance.instance-id` uniqueness
- Verify `@LoadBalanced` annotation on RestTemplate

## Production Considerations

- **High Availability**: Run multiple Eureka servers
- **Security**: Enable authentication for Eureka dashboard
- **Monitoring**: Set up alerts for service registration/deregistration
- **Network**: Configure proper DNS and firewall rules

Eureka eliminates the complexity of managing service locations in distributed systems, making your microservices truly location-independent and resilient.