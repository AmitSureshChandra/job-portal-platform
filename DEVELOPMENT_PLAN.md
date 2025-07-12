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

- [x] **job-service** - Job postings, search, and management
  - [x] Job entity (title, description, requirements, salary, location)
  - [x] Job posting APIs (CRUD)
  - [x] Job search and filtering
  - [x] Link jobs to companies
  - [x] Job status management

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

## Current Focus: Application Service

### Next Steps:
1. Create application-service module structure
2. Implement Application entity and repository
3. Create job application APIs
4. Add application tracking functionality
5. Link applications to users and jobs

## Database Schema Progress:
- [x] Users table (user-service)
- [x] Companies table (company-service)
- [x] Jobs table (job-service)
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

## API Endpoints Completed:
- [x] POST /api/jobs
- [x] GET /api/jobs/{id}
- [x] GET /api/jobs/company/{companyId}
- [x] GET /api/jobs/search
- [x] GET /api/jobs/active
- [x] PUT /api/jobs/{id}/status

## API Endpoints Planned:
- [ ] POST /api/applications/apply