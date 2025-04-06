package com.ufund.api.ufundapi.dao;
import java.io.IOException;
import java.util.List;

import com.ufund.api.ufundapi.model.User;

/**
 * Interface for managing users.
 *
 * @author Alexandra Mantagas
 */

public interface UserDAO{
    
    User createUser(User user) throws IOException;
    User getUser(String username) throws IOException;
    List<User> getAllUsers() throws IOException;
}