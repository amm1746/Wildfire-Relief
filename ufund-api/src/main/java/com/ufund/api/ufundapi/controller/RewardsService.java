package com.ufund.api.ufundapi.controller;

import java.util.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ufund.api.ufundapi.model.Rewards;

/**
 * RewardsService
 * 
 * Handles all commands related to Rewards
 * @author Sophia Le
 */
@RestController
@RequestMapping("/api")
public class RewardsService {

    private Map<String, Integer> purchases;
    private Map<String, List<Rewards>> rewards;

    public RewardsService() {
        this.purchases = new HashMap<>();
        this.rewards = new HashMap<>();
    }

    // Optionally add constructor for testing if you want to inject mock maps
    public RewardsService(Map<String, Integer> purchases, Map<String, List<Rewards>> rewards) {
        this.purchases = purchases;
        this.rewards = new HashMap<>();
    }

    // Modify recordPurchase to add rewards to a list
    public void recordPurchase(String helper) {
        int count = purchases.getOrDefault(helper,0) + 1;
        purchases.put(helper, count);
        if(count == 1) {
            Rewards firstPurchase = new Rewards("First Purchase", "You made your first purchase!");
            rewards.computeIfAbsent(helper, k -> new ArrayList<>()).add(firstPurchase);
        }
    }


    // Adjusting getRewards to accept a helper parameter as a query parameter
    @GetMapping("/rewards")
    public List<Rewards> getRewards(String helper) {
        return rewards.getOrDefault(helper, Collections.emptyList());
    }

    public void addFirstDonationReward(String helper) {
        List<Rewards> userRewards = rewards.computeIfAbsent(helper, k -> new ArrayList<>());
    
        boolean alreadyHas = userRewards.stream()
            .anyMatch(r -> "First Donation Reward".equals(r.getTitle()));
    
        if (!alreadyHas) {
            Rewards reward = new Rewards("First Donation Reward", "You earned a special first donation reward!");
            userRewards.add(reward);
        }
    }
}
