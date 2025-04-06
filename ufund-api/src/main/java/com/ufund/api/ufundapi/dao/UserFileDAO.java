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

    /**
     * Constructs a UserFileDAO for user data persistence.
     * 
     * @param filename the path to the JSON file used to store user data
     * @param objectMapper the object mapper for JSON 
     * @throws IOException if file cannot be loaded
     */
    public UserFileDAO(@Value("${users.file}") String filename, ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();
    }

    /**
     * Adds a new user to the file.
     * 
     * @param user the user being added
     * @return the created user
     * @throws IOException if the file cannot be saved
     */
    @Override
    public User createUser(User user) throws IOException {
        users.add(user);
        save();
        return user;
    }

    /**
     * Retrieves a user by their username.
     * 
     * @param username the username to search for
     * @return the matching username, or null if not found
     * @throws IOException if the file cannot be accessed
     */
    @Override
    public User getUser(String username) throws IOException {
        for(User user : users){
            if (user.getUsername().equalsIgnoreCase(username)){
                return user;
            }
        }
        return null;
    }

    /**
     * Returns a list of all registered users.
     * 
     * @return list of all users
     * @throws IOException if the file cannot be accessed
     */
    @Override
    public List<User> getAllUsers() throws IOException {
        return users;
    }

    /**
     * Loads user data from the file. If file does not exist or 
     * is empty, initializes an empty list.
     * 
     * @throws IOException if the file cannot be read
     */
    private void load() throws IOException {
        File file = new File(filename);

        if(file.exists()){
            users = new ArrayList<>(Arrays.asList(objectMapper.readValue(file, User[].class)));
        }
        else{
            users = new ArrayList<>();
        }
    }

    /**
     * Saves the current list of users to the JSON file.
     * 
     * @throws IOException if the file cannot be written
     */
    private void save() throws IOException {
        objectMapper.writeValue(new File(filename), users);
    }
}