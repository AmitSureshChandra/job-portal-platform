package com.jobportal.application.service;

import com.jobportal.application.dto.JobApplicationDto;
import com.jobportal.application.entity.Application;
import com.jobportal.application.entity.ApplicationStatus;
import com.jobportal.application.repository.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ApplicationService {
    
    @Autowired
    private ApplicationRepository applicationRepository;
    
    public Application applyForJob(JobApplicationDto dto) {
        if (applicationRepository.existsByUserIdAndJobId(dto.getUserId(), dto.getJobId())) {
            throw new RuntimeException("User has already applied for this job");
        }
        
        Application application = new Application();
        application.setUserId(dto.getUserId());
        application.setJobId(dto.getJobId());
        application.setCompanyId(dto.getCompanyId());
        application.setCoverLetter(dto.getCoverLetter());
        application.setResumeUrl(dto.getResumeUrl());
        
        return applicationRepository.save(application);
    }
    
    public Application findById(Long id) {
        return applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));
    }
    
    public List<Application> findByUserId(Long userId) {
        return applicationRepository.findByUserId(userId);
    }
    
    public List<Application> findByJobId(Long jobId) {
        return applicationRepository.findByJobId(jobId);
    }
    
    public List<Application> findByCompanyId(Long companyId) {
        return applicationRepository.findByCompanyId(companyId);
    }
    
    public Application updateApplicationStatus(Long applicationId, ApplicationStatus status) {
        Application application = findById(applicationId);
        application.setStatus(status);
        application.setUpdatedAt(LocalDateTime.now());
        return applicationRepository.save(application);
    }
    
    public Application withdrawApplication(Long applicationId, Long userId) {
        Application application = findById(applicationId);
        if (!application.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized to withdraw this application");
        }
        application.setStatus(ApplicationStatus.WITHDRAWN);
        application.setUpdatedAt(LocalDateTime.now());
        return applicationRepository.save(application);
    }
}