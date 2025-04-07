package com.ufund.api.ufundapi.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ufund.api.ufundapi.dao.NotificationDAO;
import com.ufund.api.ufundapi.model.Notification;

/**
 * Controller for managing notification-related operations
 * 
 * @author Alexandra Mantagas
 */

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationDAO notificationDAO;

    public NotificationController(NotificationDAO notificationDAO) {
        this.notificationDAO = notificationDAO;
    }

    @PostMapping
    public ResponseEntity<Notification> createNotification(@RequestBody Notification notification) throws IOException {
        Notification createdNotification = notificationDAO.createNotification(notification);
        return ResponseEntity.ok(createdNotification);
    }

    @GetMapping
    public ResponseEntity<List<Notification>> getNotifications() throws IOException {
        return ResponseEntity.ok(notificationDAO.getAllNotifications());
    }
}
