package com.jobportal.job.controller;

import com.jobportal.job.dto.JobPostingDto;
import com.jobportal.job.entity.Job;
import com.jobportal.job.entity.JobStatus;
import com.jobportal.job.entity.JobType;
import com.jobportal.job.service.JobService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {
    
    @Autowired
    private JobService jobService;
    
    @PostMapping
    public ResponseEntity<Job> postJob(@Valid @RequestBody JobPostingDto dto) {
        Job job = jobService.postJob(dto);
        return ResponseEntity.ok(job);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Job> getJobById(@PathVariable Long id) {
        Job job = jobService.findById(id);
        return ResponseEntity.ok(job);
    }
    
    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<Job>> getJobsByCompany(@PathVariable Long companyId) {
        List<Job> jobs = jobService.findByCompanyId(companyId);
        return ResponseEntity.ok(jobs);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<Job>> searchJobs(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) JobType type) {
        
        List<Job> jobs = jobService.searchJobs(title, location, type);
        return ResponseEntity.ok(jobs);
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<Job>> getActiveJobs() {
        List<Job> jobs = jobService.getActiveJobs();
        return ResponseEntity.ok(jobs);
    }
    
    @PutMapping("/{id}/status")
    public ResponseEntity<Job> updateJobStatus(@PathVariable Long id, @RequestParam JobStatus status) {
        Job job = jobService.updateJobStatus(id, status);
        return ResponseEntity.ok(job);
    }
}