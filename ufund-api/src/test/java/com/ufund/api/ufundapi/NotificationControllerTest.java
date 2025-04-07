package com.ufund.api.ufundapi;

import com.ufund.api.ufundapi.controller.NotificationController;

import com.ufund.api.ufundapi.dao.NotificationDAO;
import com.ufund.api.ufundapi.model.Notification;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class NotificationControllerTest {

    private NotificationDAO mockDao;
    private NotificationController controller;
    private Notification sampleNotification;

    @BeforeEach
    public void setup() {
        mockDao = mock(NotificationDAO.class);
        controller = new NotificationController(mockDao);

        sampleNotification = new Notification(
            "Test message", 
            "helper", 
            List.of("manager")
        );
    }

    @Test
    public void testCreateNotification() throws IOException {
        when(mockDao.createNotification(sampleNotification)).thenReturn(sampleNotification);

        ResponseEntity<Notification> response = controller.createNotification(sampleNotification);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(sampleNotification, response.getBody());
        verify(mockDao, times(1)).createNotification(sampleNotification);
    }

    @Test
    public void testGetNotifications() throws IOException {
        List<Notification> fakeList = List.of(sampleNotification);
        when(mockDao.getAllNotifications()).thenReturn(fakeList);

        ResponseEntity<List<Notification>> response = controller.getNotifications();

        assertEquals(200, response.getStatusCodeValue());


        List<Notification> body = java.util.Optional.ofNullable(response.getBody())
            .orElseThrow(() -> new AssertionError("Response body can't be null."));

        assertFalse(body.isEmpty(), "Response body list should not be empty");
        assertEquals(1, body.size());
        assertEquals("helper", body.get(0).getSender());
        verify(mockDao, times(1)).getAllNotifications();
    }

    @Test
    public void testClearNotificationsSuccess() throws IOException {
        doNothing().when(mockDao).clearNotifications();

        ResponseEntity<Void> response = controller.clearNotifications();

        assertEquals(200, response.getStatusCodeValue());
        verify(mockDao, times(1)).clearNotifications();
    }
}
