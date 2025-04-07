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
import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.Rewards;
import com.ufund.api.ufundapi.model.RewardsService;

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
    private final RewardsService rewardsService;

    public BasketController(CupboardDAO cupboardDAO, RewardsService rewardsService) {
        this.cupboardDAO = cupboardDAO;
        this.rewardsService = rewardsService;
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
    public Map<String, Object> checkoutBasket(HttpSession session) {
        List<Need> basket = getBasket(session);
    
        Map<String, Integer> needCountMap = new HashMap<>();
        for (Need need : basket) {
            needCountMap.put(need.getName(), needCountMap.getOrDefault(need.getName(), 0) + 1);
        }
    
        // Check if all items are available in the cupboard
        for (Map.Entry<String, Integer> entry : needCountMap.entrySet()) {
            String name = entry.getKey();
            int requestedQty = entry.getValue();
            Need cupboardNeed = cupboardDAO.getNeed(name);
    
            if (cupboardNeed == null || cupboardNeed.getQuantity() < requestedQty) {
                return Map.of("message", "Checkout failed: Need '" + name + "' is unavailable or has insufficient quantity.");
            }
        }
    
        // Update the cupboard stock
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
    
        // Clear basket
        basket.clear();
        session.setAttribute(BASKET_KEY, basket);
    
        // Record the purchase
        String helper = (String) session.getAttribute("helper-id");  // Extract helper ID from session
        rewardsService.recordPurchase(helper);
    
        // Add first donation reward if applicable
        rewardsService.addFirstDonationReward(helper);
    
        // Get rewards after the checkout
        List<Rewards> rewards = rewardsService.getRewards(helper);  // Get rewards based on helper
    
        // Return the response with message and rewards
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Checkout successful");
        response.put("rewards", rewards.isEmpty() ? "No rewards available" : rewards);
    
        return response;
    }
    

    

    public List<Rewards> getRewards(HttpSession session) {
        return rewardsService.getRewards("HELPER");
    }
}
