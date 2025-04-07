package com.ufund.api.ufundapi.controller;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.ufund.api.ufundapi.dao.UserDAO;
import com.ufund.api.ufundapi.model.User;

import jakarta.servlet.http.HttpSession;

/**
 * LoginController
 * 
 * Controller for handling user authentication and login.
 * Users can log in as a U-Fund Manager or a helper.
 * 
 * @author Alexandra Mantagas
 */

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class LoginController{

    private final UserDAO userDAO;

    public LoginController(UserDAO userDAO){
        this.userDAO = userDAO;
    }

    private static final String ADMIN = "U-Fund Manager";
    private static final String HELPER = "Helper";

    @Value("${admin.password}")
    private String ADMIN_PASSWORD;



    /**
     * Handles user login and determiens if user is an Admin or a Helper.
     * 
     * @param loginData The username and password entered.
     * @param session The Http session used to track the login state.
     * @return A response saying if the login was a success or a fail, with the role.
     */
    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> loginData, HttpSession session) throws IOException{
        String username = loginData.get("username");
        String password = loginData.get("password");

        if(username == null || username.isEmpty() || password == null || password.isEmpty()) {
            return createResponse("Username and password are required", null);
        }
        if(username.equalsIgnoreCase("admin") && password.equals(ADMIN_PASSWORD)){
            session.setAttribute("role", ADMIN);
        }
        else {
            User user = userDAO.getUser(username);
            if(user != null && user.getPassword().equals(password)){
                session.setAttribute("role", HELPER);
            }
            else{
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password.");
            }
        }
        session.setAttribute("username", username);
        return createResponse("Login successful", (String) session.getAttribute("role"));
    }

    /**
     * Handles user logout and clears role.
     * 
     * @param session The HttpSession to be cleared.
     * @return A response saying the user has been logged out successfully.
     */
    @PostMapping("/logout")
    public Map<String, String> logout(HttpSession session){
        session.removeAttribute("role");
        return createResponse("Logged out successfully", null);
    }

    /**
     * Gets the user's role.
     * 
     * @param session The HttpSession storing user information.
     * @return A response saying the user's role, or an error message if no user
     * is logged in.
     */
    @GetMapping("/role")
    public Map<String, String> getRole(HttpSession session){
        String role = (String) session.getAttribute("role");
        if(role == null){
            return createResponse("No user logged in", null);
        }
        return createResponse(null, role);
    }
    
    /**
     * Helper method for creating responses with a message and role.
     * 
     * @param message A message (optional).
     * @param role The user role (optional).
     * @return A map containing response.
     */
    private Map<String, String> createResponse(String message, String role){
        Map<String, String> response = new HashMap<>();
        if(message != null){
            response.put("message", message);
        }
        if (role != null){
            response.put("role", role);
        }
        return response;
    }

    /**
     * Handles registration of new Helper accounts.
     * 
     * @param registrationData A map containing username and passwords.
     * @return A response map with a success message and role information.
     * @throws IOException if user file cannot be written to.
     * @throws ResponseStatusException if input is invalid or the username is not allowed.
     */
    @PostMapping("/register")
    public Map<String, String> register(@RequestBody Map<String, String> registrationData) throws IOException{
        String username = registrationData.get("username");
        String password = registrationData.get("password");

        if(username == null || username.isEmpty() || password == null || password.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username and password are required");
        }

        if(username.equalsIgnoreCase("admin")){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username cannot be admin");
        }
        
        User newUser = new User(username, password, HELPER);
        userDAO.createUser(newUser);

        return createResponse("Helper account created successfully", HELPER);
    }
}

