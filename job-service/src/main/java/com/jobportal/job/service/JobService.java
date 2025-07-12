package com.jobportal.job.service;

import com.jobportal.job.dto.JobPostingDto;
import com.jobportal.job.entity.Job;
import com.jobportal.job.entity.JobStatus;
import com.jobportal.job.entity.JobType;
import com.jobportal.job.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class JobService {
    
    @Autowired
    private JobRepository jobRepository;
    
    public Job postJob(JobPostingDto dto) {
        Job job = new Job();
        job.setTitle(dto.getTitle());
        job.setDescription(dto.getDescription());
        job.setRequirements(dto.getRequirements());
        job.setMinSalary(dto.getMinSalary());
        job.setMaxSalary(dto.getMaxSalary());
        job.setLocation(dto.getLocation());
        job.setType(dto.getType());
        job.setCompanyId(dto.getCompanyId());
        job.setPostedBy(dto.getPostedBy());
        job.setExpiresAt(dto.getExpiresAt());
        
        return jobRepository.save(job);
    }
    
    public Job findById(Long id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found"));
    }
    
    public List<Job> findByCompanyId(Long companyId) {
        return jobRepository.findByCompanyId(companyId);
    }
    
    public List<Job> searchJobs(String title, String location, JobType type) {
        return jobRepository.searchJobs(title, location, type);
    }
    
    public Job updateJobStatus(Long jobId, JobStatus status) {
        Job job = findById(jobId);
        job.setStatus(status);
        return jobRepository.save(job);
    }
    
    public List<Job> getActiveJobs() {
        return jobRepository.findByStatus(JobStatus.ACTIVE);
    }
}