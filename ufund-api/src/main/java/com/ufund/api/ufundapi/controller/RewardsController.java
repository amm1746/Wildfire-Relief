package com.ufund.api.ufundapi.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ufund.api.ufundapi.model.Rewards;

public class RewardsController {
    private final Map<String, Integer> purchases = new HashMap<>();
    private final Map<String, List<Rewards>> rewards = new HashMap<>();

    public void recordPurchase(String helper) {
        int p =  purchases.getOrDefault(helper, 0) + 1;
        purchases.put(helper, p);

        List<Rewards> r = rewards.computeIfAbsent(helper, k -> new ArrayList<>());
        
        // First Purchase Reward
        if(p == 1) {
            r.add(new Rewards("First Purchase", "Thanks for funding your first need!"));
        }

        // Most Purchases Reward (reset for everyone else)
        int max = purchases.values().stream().max(Integer::compare).orElse(0);
        for (String user: purchases.keySet()) {
            List<Rewards> userRewards = rewards.getOrDefault(user, new ArrayList<>());
            userRewards.removeIf(a -> a.getTitle().equals("Most Purchases"));

            if(purchases.get(user) == max) {
                userRewards.add(new Rewards("Most Purchases", "You've funded the most needs!"));
                rewards.put(user, userRewards);
            }
        }
    }

    public List<Rewards> getRewards(String helper) {
        return rewards.getOrDefault(helper, new ArrayList<>());
    }
}
