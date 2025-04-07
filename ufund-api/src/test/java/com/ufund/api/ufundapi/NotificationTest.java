package com.ufund.api.ufundapi;

import com.ufund.api.ufundapi.model.Notification;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class NotificationTest {

    @Test
    public void testConstructorAndGetters() {
        Notification notification = new Notification(
            "Hello World",
            "Admin",
            List.of("UserA", "UserB")
        );

        assertEquals("Hello World", notification.getMessage());
        assertEquals("Admin", notification.getSender());
        assertEquals(List.of("UserA", "UserB"), notification.getRecipients());
    }

    @Test
    public void testSetters() {
        Notification notification = new Notification();
        notification.setMessage("Updated message");
        notification.setSender("Helper1");
        notification.setRecipients(List.of("Manager1", "Manager2"));

        assertEquals("Updated message", notification.getMessage());
        assertEquals("Helper1", notification.getSender());
        assertEquals(List.of("Manager1", "Manager2"), notification.getRecipients());
    }
}