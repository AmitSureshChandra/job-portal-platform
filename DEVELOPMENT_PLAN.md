# Job Portal Development Plan

## Project Status: In Progress

### Phase 1: Core Foundation ✅
- [x] **eureka-server** - Service discovery and registration
- [x] **config-server** - Centralized configuration management  
- [x] **user-service** - User registration, authentication, profile management
  - [x] User entity with roles (JOB_SEEKER, RECRUITER, ADMIN)
  - [x] User registration API
  - [x] User lookup APIs (by ID, username)
  - [x] Password encryption with BCrypt
  - [x] Basic security configuration

### Phase 2: Business Logic Modules
- [x] **company-service** - Company profiles and management
  - [x] Company entity (name, description, location, industry, size)
  - [x] Company registration API
  - [x] Company profile management
  - [x] Company search and listing
  - [x] Link companies to recruiter users

- [ ] **job-service** - Job postings, search, and management
  - [ ] Job entity (title, description, requirements, salary, location)
  - [ ] Job posting APIs (CRUD)
  - [ ] Job search and filtering
  - [ ] Link jobs to companies
  - [ ] Job status management

- [ ] **application-service** - Job applications and tracking
  - [ ] Application entity (user, job, status, applied date)
  - [ ] Apply for job API
  - [ ] Application status tracking
  - [ ] Application history for users
  - [ ] Application management for recruiters

### Phase 3: Advanced Features
- [ ] **search-service** - Advanced search using Elasticsearch
  - [ ] Elasticsearch integration
  - [ ] Advanced job search with filters
  - [ ] Search analytics and suggestions
  - [ ] Full-text search capabilities

- [ ] **notification-service** - Email/SMS notifications via Kafka
  - [ ] Kafka integration
  - [ ] Email notification templates
  - [ ] Application status notifications
  - [ ] Job alert notifications

- [ ] **api-gateway** - Single entry point and security
  - [ ] Request routing to services
  - [ ] Authentication and authorization
  - [ ] Rate limiting
  - [ ] Request/response logging

## Current Focus: Job Service

### Next Steps:
1. Create job-service module structure
2. Implement Job entity and repository
3. Create job posting and management APIs
4. Add job search functionality
5. Link jobs to companies

## Database Schema Progress:
- [x] Users table (user-service)
- [x] Companies table (company-service)
- [ ] Jobs table (job-service)
- [ ] Applications table (application-service)

## API Endpoints Completed:
- [x] POST /api/users/register
- [x] GET /api/users/{id}
- [x] GET /api/users/username/{username}

## API Endpoints Completed:
- [x] POST /api/companies/register
- [x] GET /api/companies/{id}
- [x] GET /api/companies/owner/{ownerId}
- [x] GET /api/companies/search

## API Endpoints Planned:
- [ ] POST /api/jobs
- [ ] GET /api/jobs/search
- [ ] POST /api/applications/apply