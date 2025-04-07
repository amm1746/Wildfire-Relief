package com.ufund.api.ufundapi.dao;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.Notification;


/**
 * Implementation of NotificationDAO.java
 *
 * @author Alexandra Mantagas
 */

@Repository
public class NotificationFileDAO implements NotificationDAO {

    private final ObjectMapper objectMapper;
    private final String filename;
    private List<Notification> notifications;

    public NotificationFileDAO(@Value("${notifications.file}") String filename, ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();
    }

    private void load() throws IOException {
        File file = new File(filename);
        if (file.exists() && file.length() > 0){
            notifications = new ArrayList<>(Arrays.asList(objectMapper.readValue(file, Notification[].class)));
        } 
        else{
            notifications = new ArrayList<>();
        }
    }

    private void save() throws IOException {
        objectMapper.writeValue(new File(filename), notifications);
    }

    @Override
    public Notification createNotification(Notification notification) throws IOException {
        notifications.add(notification);
        save();
        return notification;
    }

    @Override
    public List<Notification> getAllNotifications() throws IOException {
        return notifications;
    }
}