package com.ufund.api.ufundapi;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.dao.NotificationFileDAO;
import com.ufund.api.ufundapi.model.Notification;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class NotificationFileDAOTest {
    private NotificationFileDAO dao;
    private File tempFile;

    @BeforeEach
    public void setup() throws Exception {
        tempFile = File.createTempFile("test-notifications", ".json");
        tempFile.deleteOnExit();
        dao = new NotificationFileDAO(tempFile.getAbsolutePath(), new ObjectMapper());
    }

    @Test
    public void testCreateNotification() throws Exception {
        Notification n = new Notification("Welcome", "System", List.of("testUser"));

        Notification result = dao.createNotification(n);

        assertNotNull(result);
        assertEquals("System", result.getSender());

        List<Notification> all = dao.getAllNotifications();
        assertEquals(1, all.size());
        assertEquals("Welcome", all.get(0).getMessage());
    }

    @Test
    public void testGetAllNotifications() throws Exception {
        dao.createNotification(new Notification("One", "Sender1", List.of("User")));
        dao.createNotification(new Notification("Two", "Sender2", List.of("User")));

        List<Notification> notifications = dao.getAllNotifications();
        assertEquals(2, notifications.size());
    }

    @Test
    public void testClearNotifications() throws Exception {
        dao.createNotification(new Notification("Clear me", "Helper", List.of("Manager")));
        dao.clearNotifications();

        List<Notification> notifications = dao.getAllNotifications();
        assertTrue(notifications.isEmpty(), "Notification list should be empty after clear");
    }

    @Test
    public void testLoadHandlesMissingFile() throws Exception {
        tempFile.delete();  
        dao = new NotificationFileDAO(tempFile.getAbsolutePath(), new ObjectMapper());

        List<Notification> result = dao.getAllNotifications();
        assertNotNull(result);
        assertTrue(result.isEmpty(), "Should handle missing file by returning empty list");
    }
}
