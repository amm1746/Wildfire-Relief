package com.ufund.api.ufundapi.controller;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")

public class LoginController{

    private String userType;
    private static final String ADMIN_PASSWORD = "admin123";
    private static final String HELPER_PASSWORD = "helper123";


    @PostMapping("/login")
    public Map<String, String> login(@RequestParam String username, @RequestParam String password){
        if(username == null || username.isEmpty() || password == null || password.isEmpty()) {
            return createResponse("Username and password are required", null);
        }
        if(username.equalsIgnoreCase("admin") && password.equals(ADMIN_PASSWORD)){
            userType = "U-fund Manager";
        }
        else if(!username.equalsIgnoreCase("admin") && password.equals(HELPER_PASSWORD)){
            userType = "Helper";
        }
        else{
            return createResponse("Invalid username or password", null);
        }
        return createResponse("Login successful", userType);
    }

    @PostMapping("/logout")
    public Map<String, String> logout(){
        userType = null;
        return createResponse("Logged out sucessfully", null);
    }

    @GetMapping("/role")
    public Map<String, String> getRole(){
        if(userType == null){
            return createResponse("No user logged in", null);
        }
        return createResponse(null, userType);
    }

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