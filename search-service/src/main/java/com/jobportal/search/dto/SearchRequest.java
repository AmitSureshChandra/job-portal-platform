package com.jobportal.search.dto;

import java.math.BigDecimal;
import java.util.List;

public class SearchRequest {
    private String query;
    private String location;
    private List<String> jobTypes;
    private String industry;
    private BigDecimal minSalary;
    private BigDecimal maxSalary;
    private int page = 0;
    private int size = 20;
    
    // Getters and Setters
    public String getQuery() { return query; }
    public void setQuery(String query) { this.query = query; }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public List<String> getJobTypes() { return jobTypes; }
    public void setJobTypes(List<String> jobTypes) { this.jobTypes = jobTypes; }
    
    public String getIndustry() { return industry; }
    public void setIndustry(String industry) { this.industry = industry; }
    
    public BigDecimal getMinSalary() { return minSalary; }
    public void setMinSalary(BigDecimal minSalary) { this.minSalary = minSalary; }
    
    public BigDecimal getMaxSalary() { return maxSalary; }
    public void setMaxSalary(BigDecimal maxSalary) { this.maxSalary = maxSalary; }
    
    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }
    
    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }
}