package com.jobportal.job.dto;

import com.jobportal.job.entity.JobType;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class JobPostingDto {
    @NotBlank
    private String title;
    
    private String description;
    private String requirements;
    private BigDecimal minSalary;
    private BigDecimal maxSalary;
    private String location;
    private JobType type;
    private Long companyId;
    private Long postedBy;
    private LocalDateTime expiresAt;
    
    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getRequirements() { return requirements; }
    public void setRequirements(String requirements) { this.requirements = requirements; }
    
    public BigDecimal getMinSalary() { return minSalary; }
    public void setMinSalary(BigDecimal minSalary) { this.minSalary = minSalary; }
    
    public BigDecimal getMaxSalary() { return maxSalary; }
    public void setMaxSalary(BigDecimal maxSalary) { this.maxSalary = maxSalary; }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public JobType getType() { return type; }
    public void setType(JobType type) { this.type = type; }
    
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    
    public Long getPostedBy() { return postedBy; }
    public void setPostedBy(Long postedBy) { this.postedBy = postedBy; }
    
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }
}