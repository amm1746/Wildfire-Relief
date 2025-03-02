package com.ufund.api.ufundapi.controller;
import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")

public class LoginController{

    private String userType;

    @PostMapping("/login")
    public Map<String, String> login(@RequestParam String username){
        if(username == null || username.isEmpty()) {
            return createResponse("Username is required", null);
        }
        if(username.equalsIgnoreCase("admin")){
            userType = "Manager";
        }
        else{
            userType = "Helper";
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