package com.ufund.api.ufundapi.controller;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
@CrossOrigin(origins = "*")

public class LoginController{

    private static final String ADMIN_PASSWORD = "admin123";
    private static final String HELPER_PASSWORD = "helper123";

    // will later store credentials in file instead

    /**
     * Handles user login and determiens if user is an Admin or a Helper.
     * 
     * @param username The username entered.
     * @param password The password entered.
     * @return A response saying if the login was a success or a fail, with the role.
     */
    @PostMapping("/login")
    public Map<String, String> login(@RequestParam String username, @RequestParam String password, HttpSession session){
        if(username == null || username.isEmpty() || password == null || password.isEmpty()) {
            return createResponse("Username and password are required", null);
        }
        if(username.equalsIgnoreCase("admin") && password.equals(ADMIN_PASSWORD)){
            session.setAttribute("role", "U-Fund Manager");
        }
        else if(!username.equalsIgnoreCase("admin") && password.equals(HELPER_PASSWORD)){
            session.setAttribute("role", "Helper");
        }
        else{
            return createResponse("Invalid username or password", null);
        }
        return createResponse("Login successful", (String) session.getAttribute("role"));
    }

    /**
     * Handles user logout and clears role.
     * 
     * @param session The HTTP session.
     * @return A response saying the user has been logged out successfully.
     */
    @PostMapping("/logout")
    public Map<String, String> logout(HttpSession session){
        session.removeAttribute("role");
        return createResponse("Logged out sucessfully", null);
    }

    /**
     * Gets the user's role.
     * 
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
     * @return A map containing response details.
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
}