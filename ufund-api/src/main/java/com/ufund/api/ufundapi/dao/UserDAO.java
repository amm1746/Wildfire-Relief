package com.ufund.api.ufundapi.dao;
import java.io.IOException;
import java.util.List;

import com.ufund.api.ufundapi.model.User;

/**
 * Interface for data access related to User objects.
 *
 * @author Alexandra Mantagas
 */

public interface UserDAO{
    
    /**
     * Creates a new user account.
     * 
     * @param user the User object being created
     * @return the created User
     * @throws IOException if an error occurs when saving user
     */
    User createUser(User user) throws IOException;

    /**
     * Retrieves the user account by username.
     * 
     * @param username the username to look up
     * @return the matching User object, or null if not found
     * @throws IOException if an error occurs
     */
    User getUser(String username) throws IOException;

    /**
     * Retrieves all user accounts.
     * 
     * @return a list of all Users
     * @throws IOException if an error occurs
     */
    List<User> getAllUsers() throws IOException;
}