# Job Portal Platform

A multi-module microservices-based job portal platform similar to Naukari, built with Spring Boot and Gradle.

## Architecture

### Microservices
- **eureka-server**: Service discovery and registration
- **config-server**: Centralized configuration management
- **api-gateway**: Single entry point for all client requests
- **user-service**: User registration, authentication, and profile management
- **company-service**: Company profiles and management
- **job-service**: Job postings, search, and management
- **application-service**: Job applications and tracking
- **search-service**: Advanced search using Elasticsearch
- **notification-service**: Email/SMS notifications via Kafka

## Technology Stack
- Java 17
- Spring Boot 3.2.0
- Spring Cloud 2023.0.0
- Gradle 8.5
- Postgres (for data persistence)
- Elasticsearch (for search)
- Kafka (for messaging)

## Build & Run

```bash
# Build all modules
./gradlew build

# Run specific service
./gradlew :user-service:bootRun

# Run all services (requires Docker Compose setup)
docker-compose up
```

## Service Ports
- Eureka Server: 8761
- Config Server: 8888
- API Gateway: 8080
- User Service: 8081
- Company Service: 8082
- Job Service: 8083
- Application Service: 8084
- Search Service: 8085
- Notification Service: 8086

## Imp commands
```shell
docker run -d \
--name mysql-db \
-e MYSQL_ROOT_PASSWORD=root \    
-p 3306:3306 \
mysql:8.0
```

```shell
docker run -d \
  --name eureka-server \
  -p 8761:8761 \
  springcloud/eureka
```

```shell
docker run -d \
  --name elasticsearch \
  -e "discovery.type=single-node" \
  -e "xpack.security.enabled=false" \
  -p 9200:9200 \
  -p 9300:9300 \
  elasticsearch:8.11.0
```

```shell
# Zookeeper
docker run -d \
  --name zookeeper \
  -p 2181:2181 \
  confluentinc/cp-zookeeper:latest \
  -e ZOOKEEPER_CLIENT_PORT=2181

# Kafka
docker run -d \
  --name kafka \
  -p 9092:9092 \
  -e KAFKA_ZOOKEEPER_CONNECT=zookeeper:2181 \
  -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092 \
  -e KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR=1 \
  confluentinc/cp-kafka:latest
```


