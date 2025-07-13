package com.jobportal.application.dto;

import jakarta.validation.constraints.NotNull;

public class JobApplicationDto {
    @NotNull
    private Long userId;
    
    @NotNull
    private Long jobId;
    
    @NotNull
    private Long companyId;
    
    private String coverLetter;
    private String resumeUrl;
    
    // Getters and Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public Long getJobId() { return jobId; }
    public void setJobId(Long jobId) { this.jobId = jobId; }
    
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    
    public String getCoverLetter() { return coverLetter; }
    public void setCoverLetter(String coverLetter) { this.coverLetter = coverLetter; }
    
    public String getResumeUrl() { return resumeUrl; }
    public void setResumeUrl(String resumeUrl) { this.resumeUrl = resumeUrl; }
}