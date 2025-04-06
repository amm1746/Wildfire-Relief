package com.ufund.api.ufundapi.dao;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.User;

/**
 * Implementation of UserFileDAO.java
 *
 * @author Alexandra Mantagas
 */

@Repository
public class UserFileDAO implements UserDAO {

    private final ObjectMapper objectMapper;
    private final String filename;
    private List<User> users;

    public UserFileDAO(@Value("${users.file}") String filename, ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();
    }

    @Override
    public User createUser(User user) throws IOException {
        users.add(user);
        save();
        return user;
    }

    @Override
    public User getUser(String username) throws IOException {
        for(User user : users){
            if (user.getUsername().equalsIgnoreCase(username)){
                return user;
            }
        }
        return null;
    }

    @Override
    public List<User> getAllUsers() throws IOException {
        return users;
    }

    private void load() throws IOException {
        File file = new File(filename);

        if(file.exists()){
            users = new ArrayList<>(Arrays.asList(objectMapper.readValue(file, User[].class)));
        }
        else{
            users = new ArrayList<>();
        }
    }

    private void save() throws IOException {
        objectMapper.writeValue(new File(filename), users);
    }
}