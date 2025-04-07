package com.ufund.api.ufundapi.model;

import java.util.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
        List<Rewards> rewardList = getRewards(helper); // Retrieve the current reward list
        Rewards firstPurchaseReward = new Rewards("First Purchase", "You made your first purchase!");
        rewardList.add(firstPurchaseReward); // Add the new reward to the list
        // Save the updated list back if necessary (depends on your implementation)
    }


    // Adjusting getRewards to accept a helper parameter as a query parameter
    @GetMapping("/rewards")
    public List<Rewards> getRewards(String helper) {
        List<Rewards> rewardsList = new ArrayList<>();

        return rewardsList;
    }


    public void addFirstDonationReward(String helper) {
        // Compute if absent, but ensure a valid Rewards object
        Rewards userReward = rewards.computeIfAbsent(helper, k -> new Rewards("No Reward", "No description"));
    
        // Check if the user has already received the first donation reward
        if (userReward.getTitle().equals("No Reward") || !userReward.getTitle().equals("First Donation Reward")) {
            userReward = new Rewards("First Donation Reward", "You earned a special first donation reward!");
            rewards.put(helper, userReward);  // Ensure updated reward is put back
        }
    }
    
}
