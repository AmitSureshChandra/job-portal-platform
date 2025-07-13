package com.jobportal.notification.service;

import com.jobportal.notification.dto.NotificationMessage;
import com.jobportal.notification.entity.NotificationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    
    @Autowired
    private KafkaTemplate<String, NotificationMessage> kafkaTemplate;
    
    @Autowired
    private EmailService emailService;
    
    public void sendNotification(NotificationMessage message) {
        kafkaTemplate.send("job-notifications", message);
    }
    
    public void processNotification(NotificationMessage message) {
        switch (message.getType()) {
            case APPLICATION_SUBMITTED:
                handleApplicationSubmitted(message);
                break;
            case APPLICATION_STATUS_CHANGED:
                handleApplicationStatusChanged(message);
                break;
            case JOB_ALERT:
                handleJobAlert(message);
                break;
            case INTERVIEW_SCHEDULED:
                handleInterviewScheduled(message);
                break;
            case JOB_POSTED:
                handleJobPosted(message);
                break;
        }
    }
    
    private void handleApplicationSubmitted(NotificationMessage message) {
        String subject = "Application Submitted Successfully";
        String body = "Your application has been submitted successfully. We will review it and get back to you soon.";
        emailService.sendEmail(message.getTo(), subject, body);
    }
    
    private void handleApplicationStatusChanged(NotificationMessage message) {
        emailService.sendEmail(message.getTo(), message.getSubject(), message.getBody());
    }
    
    private void handleJobAlert(NotificationMessage message) {
        String subject = "New Job Alert - " + message.getSubject();
        emailService.sendEmail(message.getTo(), subject, message.getBody());
    }
    
    private void handleInterviewScheduled(NotificationMessage message) {
        String subject = "Interview Scheduled";
        emailService.sendEmail(message.getTo(), subject, message.getBody());
    }
    
    private void handleJobPosted(NotificationMessage message) {
        String subject = "New Job Posted";
        emailService.sendEmail(message.getTo(), subject, message.getBody());
    }
}