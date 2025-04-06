package com.ufund.api.ufundapi.model;

import java.util.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RewardsService {
    private final Map<String, Integer> purchases = new HashMap<>();
    private final Map<String, List<Rewards>> rewards = new HashMap<>();

    public void recordPurchase(String helper) {
        int p = purchases.getOrDefault(helper, 0) + 1;
        purchases.put(helper, p);

        List<Rewards> r = rewards.computeIfAbsent(helper, k -> new ArrayList<>()); // Ensure the helper has a reward list

        // First Purchase Reward
        if (p == 1) {
            r.add(new Rewards("First Purchase", "Thanks for funding your first need!"));
        }

        // Most Purchases Reward (reset for everyone else)
        int max = purchases.values().stream().max(Integer::compare).orElse(0);
        for (String user : purchases.keySet()) {
            List<Rewards> userRewards = rewards.getOrDefault(user, new ArrayList<>());
            userRewards.removeIf(a -> a.getTitle().equals("Most Purchases"));

            if (purchases.get(user) == max) {
                userRewards.add(new Rewards("Most Purchases", "You've funded the most needs!"));
                rewards.put(user, userRewards);
            }
        }
    }

    // Adjusting getRewards to accept a helper parameter as a query parameter
    @GetMapping("/rewards")
    public List<Rewards> getRewards(@RequestParam String helper) {
        return rewards.getOrDefault(helper, new ArrayList<>());
    }
}
