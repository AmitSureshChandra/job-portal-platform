package com.jobportal.notification.controller;

import com.jobportal.notification.dto.NotificationMessage;
import com.jobportal.notification.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    
    @Autowired
    private NotificationService notificationService;
    
    @PostMapping("/send")
    public ResponseEntity<String> sendNotification(@RequestBody NotificationMessage message) {
        notificationService.sendNotification(message);
        return ResponseEntity.ok("Notification sent successfully");
    }
    
    @PostMapping("/direct")
    public ResponseEntity<String> sendDirectNotification(@RequestBody NotificationMessage message) {
        notificationService.processNotification(message);
        return ResponseEntity.ok("Notification processed successfully");
    }
}