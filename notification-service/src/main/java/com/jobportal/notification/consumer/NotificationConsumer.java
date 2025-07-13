package com.jobportal.notification.consumer;

import com.jobportal.notification.dto.NotificationMessage;
import com.jobportal.notification.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationConsumer {
    
    @Autowired
    private NotificationService notificationService;
    
    @KafkaListener(topics = "job-notifications", groupId = "notification-group")
    public void consumeNotification(NotificationMessage message) {
        System.out.println("Received notification: " + message.getType() + " for " + message.getTo());
        notificationService.processNotification(message);
    }
}