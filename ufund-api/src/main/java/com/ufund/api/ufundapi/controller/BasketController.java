package com.ufund.api.ufundapi.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.ufund.api.ufundapi.dao.CupboardDAO;
import com.ufund.api.ufundapi.dao.NotificationDAO;
import com.ufund.api.ufundapi.dao.UserDAO;
import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.Notification;
import com.ufund.api.ufundapi.model.User;

import jakarta.servlet.http.HttpSession;
/**
 * BasketController
 * 
 * Handles all commands related to a helper and their funding basket.
 * 
 * @author Alexandra Mantagas
 */

@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RestController
@RequestMapping("/api")
public class BasketController {

    private static final String BASKET_KEY = "basket";
    private final CupboardDAO cupboardDAO;
    private final NotificationDAO notificationDAO;
    private final UserDAO userDAO;

    public BasketController(CupboardDAO cupboardDAO, NotificationDAO notificationDAO, UserDAO userDAO) {
        this.cupboardDAO = cupboardDAO;
        this.notificationDAO = notificationDAO;
        this.userDAO = userDAO;
    }


    @PostMapping("/add-to-basket")
    public Map<String, String> addToBasket(@RequestBody Need need, HttpSession session) throws IOException {
        Need existingNeed = cupboardDAO.getNeed(need.getName());
        if (existingNeed == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Need does not exist");
        }

        List<Need> basket = getBasket(session);
        int count = 0;

        for(Need n : basket){
            if(n.getName().equalsIgnoreCase(need.getName())){
                count++;
            }
        }

        if(count >= existingNeed.getQuantity()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot add more than the available quantity");
        }
        basket.add(need);
        session.setAttribute(BASKET_KEY, basket);

        return Map.of("message", "Added to basket");
    }
    @GetMapping("/basket")
    public List<Need> getBasket(HttpSession session) {
        List<Need> basket = (List<Need>) session.getAttribute(BASKET_KEY);
        if (basket == null) {
            basket = new ArrayList<>();
            session.setAttribute(BASKET_KEY, basket);
        }
        return basket;
    }

@PostMapping("/remove-from-basket")
    public Map<String, String> removeFromBasket(@RequestBody Need need, HttpSession session) {
        List<Need> basket = getBasket(session);
        
        for (int i = 0; i < basket.size(); i++) {
            if (basket.get(i).getName().equalsIgnoreCase(need.getName())) {
                basket.remove(i);
                session.setAttribute(BASKET_KEY, basket);
                return Map.of("message", "Removed need from basket");
            }
        }
        
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Need not found in basket");
    }

    @PostMapping("/checkout")
    public Map<String, String> checkoutBasket(HttpSession session) {
        String currentUser = (String) session.getAttribute("username");
        List<Need> basket = getBasket(session);
    
        Map<String, Integer> needCountMap = new HashMap<>();
        for (Need need : basket) {
            needCountMap.put(need.getName(), needCountMap.getOrDefault(need.getName(), 0) + 1);
        }
    
        for (Map.Entry<String, Integer> entry : needCountMap.entrySet()) {
            String name = entry.getKey();
            int requestedQty = entry.getValue();
            Need cupboardNeed = cupboardDAO.getNeed(name);
    
            if (cupboardNeed == null || cupboardNeed.getQuantity() < requestedQty) {
                return Map.of("message", "Checkout failed: Need '" + name + "' is unavailable or has insufficient quantity.");
            }
        }
    
        for (Map.Entry<String, Integer> entry : needCountMap.entrySet()) {
            String name = entry.getKey();
            int requestedQty = entry.getValue();
            Need cupboardNeed = cupboardDAO.getNeed(name);
    
            int remainingQty = cupboardNeed.getQuantity() - requestedQty;
    
            if (remainingQty <= 0) {
                cupboardDAO.deleteNeed(name);
            } else {
                cupboardNeed.setQuantity(remainingQty);
                cupboardDAO.updateNeed(name, cupboardNeed);
            }
        }
    
        try {
            List<String> recipients = new ArrayList<>();
            for (User user : userDAO.getAllUsers()){
                if(!user.getUsername().equalsIgnoreCase(currentUser)){
                    recipients.add(user.getUsername());
                }
            }

            String message = currentUser + " purchased items from the cupboard!";
            Notification notification = new Notification(message, currentUser, recipients);
            notificationDAO.createNotification(notification);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create notification");
        }

        basket.clear();
        session.setAttribute(BASKET_KEY, basket);
    
        return Map.of("message", "Checkout successful");
    }
}
