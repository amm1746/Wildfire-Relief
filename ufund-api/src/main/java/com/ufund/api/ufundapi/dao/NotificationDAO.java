package com.ufund.api.ufundapi.dao;
import java.io.IOException;
import java.util.List;

import com.ufund.api.ufundapi.model.Notification;

/**
 * Interface for managing notifications. 
 *
 * @author Alexandra Mantagas
 */

public interface NotificationDAO {
    
    Notification createNotification(Notification notification) throws IOException;

    List<Notification> getAllNotifications() throws IOException;

    void clearNotifications() throws IOException;
}