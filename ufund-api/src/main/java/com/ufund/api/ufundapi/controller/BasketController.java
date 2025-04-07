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

import com.ufund.api.ufundapi.dao.BasketFileDAO;
import com.ufund.api.ufundapi.dao.CupboardDAO;
import com.ufund.api.ufundapi.dao.NotificationDAO;
import com.ufund.api.ufundapi.dao.UserDAO;
import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.Notification;
import com.ufund.api.ufundapi.model.User;
import com.ufund.api.ufundapi.model.Rewards;

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
    private final RewardsService rewardsService;
    private BasketFileDAO basketDAO;

    public BasketController(CupboardDAO cupboardDAO, NotificationDAO notificationDAO, UserDAO userDAO, RewardsService rewardsService, BasketFileDAO basketDAO) {
        this.cupboardDAO = cupboardDAO;
        this.notificationDAO = notificationDAO;
        this.userDAO = userDAO;
        this.rewardsService = rewardsService;
        this.basketDAO = basketDAO;
    }


    @PostMapping("/add-to-basket")
    public Map<String, String> addToBasket(@RequestBody Need need, HttpSession session) throws IOException {
        String username = (String) session.getAttribute("username");  
        if (username == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not logged in");
        }

        Need existingNeed = cupboardDAO.getNeed(need.getName());
        if (existingNeed == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Need does not exist");
        }

        List<Need> basket = basketDAO.getBasket(username); 
        
        basket.add(need);
        basketDAO.saveBasket(username, basket);  

        return Map.of("message", "Added to basket");
    }

    @GetMapping("/basket")
    public List<Need> getBasket(HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return new ArrayList<>();
        }
        return basketDAO.getBasket(username);
    }

@PostMapping("/remove-from-basket")
public Map<String, String> removeFromBasket(@RequestBody Need need, HttpSession session) throws IOException {
    String username = (String) session.getAttribute("username");
    if (username == null) {
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not logged in");
    }

    List<Need> basket = basketDAO.getBasket(username);
    
    for (int i = 0; i < basket.size(); i++) {
        if (basket.get(i).getName().equalsIgnoreCase(need.getName())) {
            basket.remove(i);
            basketDAO.saveBasket(username, basket);  
            return Map.of("message", "Removed need from basket");
        }
    }
    
    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Need not found in basket");
}
    @PostMapping("/checkout")
    public Map<String, Object> checkoutBasket(HttpSession session) throws IOException {
        String currentUser = (String) session.getAttribute("username");
        if (currentUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not logged in");
        }
    
        List<Need> basket = basketDAO.getBasket(currentUser);  
        if (basket.isEmpty()) {
            return Map.of("success", false, "message", "Basket is empty");
        }
    
        Map<String, Integer> needCountMap = new HashMap<>();
        for (Need need : basket) {
            needCountMap.put(need.getName(), needCountMap.getOrDefault(need.getName(), 0) + 1);
        }
    
        for (Map.Entry<String, Integer> entry : needCountMap.entrySet()) {
            String name = entry.getKey();
            int requestedQty = entry.getValue();
            Need cupboardNeed = cupboardDAO.getNeed(name);
    
            if (cupboardNeed == null || cupboardNeed.getQuantity() < requestedQty) {
                return Map.of("success", false, "message", 
                    "Checkout failed: Need '" + name + "' is unavailable or has insufficient quantity.");
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
            for (User user : userDAO.getAllUsers()) {
                if (!user.getUsername().equalsIgnoreCase(currentUser)) {
                    recipients.add(user.getUsername());
                }
            }
            Notification notification = new Notification(
                currentUser + " purchased items from the cupboard!", 
                currentUser, 
                recipients
            );
            notificationDAO.createNotification(notification);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create notification");
        }
    
        basket.clear();
        basketDAO.saveBasket(currentUser, basket); 
    
        String helper = (String) session.getAttribute("helper-id");
        rewardsService.recordPurchase(helper);
        rewardsService.addFirstDonationReward(helper);
        List<Rewards> rewards = rewardsService.getRewards(helper);
    
        return Map.of(
            "success", true,
            "message", "Checkout successful",
            "rewards", rewards.isEmpty() ? "No rewards available" : rewards
        );
    }
    
    public List<Rewards> getRewards(HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return new ArrayList<>();
        }
        return rewardsService.getRewards(username); 
    }
}
