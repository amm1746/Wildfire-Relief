package com.ufund.api.ufundapi.model;

import java.util.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    private Map<String, Rewards> rewards;

    public RewardsService() {
        this.purchases = new HashMap<>();
        this.rewards = new HashMap<>();
    }

    // Optionally add constructor for testing if you want to inject mock maps
    public RewardsService(Map<String, Integer> purchases, Map<String, Rewards> rewards) {
        this.purchases = purchases;
        this.rewards = rewards;
    }

    // Modify recordPurchase to add rewards to a list
    public void recordPurchase(String helper) {
        if(rewards.get(helper) == null){
            Rewards firstPurchaseReward = new Rewards("First Purchase", "You made your first purchase!");
            rewards.put(helper, firstPurchaseReward);
        }
    
    }


    // Adjusting getRewards to accept a helper parameter as a query parameter
    @GetMapping("/rewards")
    public List<Rewards> getRewards(String helper) {
        Rewards r = rewards.get(helper);
        return(r == null) ? List.of() : List.of(r);
    }


    public void addFirstDonationReward(String helper) {
        // Compute if absent, but ensure a valid Rewards object
        Rewards userReward = rewards.get(helper);
        if(userReward != null && "First Donation Reward".equals(userReward.getTitle())){
            return;
        }

        Rewards reward = new Rewards("First Donation Reward", "You earned a special first donation reward!");
        rewards.put(helper, reward);
    }
    
}
