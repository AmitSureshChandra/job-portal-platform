package com.jobportal.company.dto;

import com.jobportal.company.entity.CompanySize;
import jakarta.validation.constraints.NotBlank;

public class CompanyRegistrationDto {
    @NotBlank
    private String name;
    
    private String description;
    private String industry;
    private String location;
    private String website;
    private CompanySize size;
    private Long ownerId;
    
    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getIndustry() { return industry; }
    public void setIndustry(String industry) { this.industry = industry; }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }
    
    public CompanySize getSize() { return size; }
    public void setSize(CompanySize size) { this.size = size; }
    
    public Long getOwnerId() { return ownerId; }
    public void setOwnerId(Long ownerId) { this.ownerId = ownerId; }
}