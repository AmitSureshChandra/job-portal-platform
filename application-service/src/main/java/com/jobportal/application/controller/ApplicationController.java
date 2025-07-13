package com.jobportal.application.controller;

import com.jobportal.application.dto.JobApplicationDto;
import com.jobportal.application.entity.Application;
import com.jobportal.application.entity.ApplicationStatus;
import com.jobportal.application.service.ApplicationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {
    
    @Autowired
    private ApplicationService applicationService;
    
    @PostMapping("/apply")
    public ResponseEntity<Application> applyForJob(@Valid @RequestBody JobApplicationDto dto) {
        Application application = applicationService.applyForJob(dto);
        return ResponseEntity.ok(application);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Application> getApplicationById(@PathVariable Long id) {
        Application application = applicationService.findById(id);
        return ResponseEntity.ok(application);
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Application>> getApplicationsByUser(@PathVariable Long userId) {
        List<Application> applications = applicationService.findByUserId(userId);
        return ResponseEntity.ok(applications);
    }
    
    @GetMapping("/job/{jobId}")
    public ResponseEntity<List<Application>> getApplicationsByJob(@PathVariable Long jobId) {
        List<Application> applications = applicationService.findByJobId(jobId);
        return ResponseEntity.ok(applications);
    }
    
    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<Application>> getApplicationsByCompany(@PathVariable Long companyId) {
        List<Application> applications = applicationService.findByCompanyId(companyId);
        return ResponseEntity.ok(applications);
    }
    
    @PutMapping("/{id}/status")
    public ResponseEntity<Application> updateApplicationStatus(
            @PathVariable Long id, 
            @RequestParam ApplicationStatus status) {
        Application application = applicationService.updateApplicationStatus(id, status);
        return ResponseEntity.ok(application);
    }
    
    @PutMapping("/{id}/withdraw")
    public ResponseEntity<Application> withdrawApplication(
            @PathVariable Long id, 
            @RequestParam Long userId) {
        Application application = applicationService.withdrawApplication(id, userId);
        return ResponseEntity.ok(application);
    }
}