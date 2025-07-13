package com.jobportal.notification.dto;

import com.jobportal.notification.entity.NotificationType;

public class NotificationMessage {
    private String to;
    private String subject;
    private String body;
    private NotificationType type;
    private Long userId;
    private Long jobId;
    private Long companyId;
    
    // Getters and Setters
    public String getTo() { return to; }
    public void setTo(String to) { this.to = to; }
    
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    
    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
    
    public NotificationType getType() { return type; }
    public void setType(NotificationType type) { this.type = type; }
    
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public Long getJobId() { return jobId; }
    public void setJobId(Long jobId) { this.jobId = jobId; }
    
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
}