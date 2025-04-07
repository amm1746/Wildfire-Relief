package com.ufund.api.ufundapi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ufund.api.ufundapi.controller.RewardsService;
import com.ufund.api.ufundapi.model.Rewards;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit tests for the Rewards system
 * Updated to reflect tracking of multiple rewards and purchase counts
 */
@SpringBootTest
public class RewardsServiceTest {

    @Autowired
    private RewardsService rewardsService;

    @Test
    public void testRecordPurchase_addsFirstPurchaseRewardOnce() {
        String helper = "user123";

        // First purchase should trigger reward
        rewardsService.recordPurchase(helper);
        List<Rewards> rewards = rewardsService.getRewards(helper);

        assertEquals(1, rewards.size(), "User should have one reward after first purchase");
        assertEquals("First Purchase", rewards.get(0).getTitle());
        assertEquals("You made your first purchase!", rewards.get(0).getDescription());

        // Second purchase should not add new reward
        rewardsService.recordPurchase(helper);
        rewards = rewardsService.getRewards(helper);
        assertEquals(1, rewards.size(), "User should still have only one reward after second purchase");
    }

    @Test
    public void testAddFirstDonationReward_addsRewardIfNotExists() {
        String helper = "donor123";

        // Add the donation reward
        rewardsService.addFirstDonationReward(helper);
        List<Rewards> rewards = rewardsService.getRewards(helper);

        assertEquals(1, rewards.size(), "Should have one donation reward");
        assertEquals("First Donation Reward", rewards.get(0).getTitle());
    }

    @Test
    public void testAddFirstDonationReward_doesNotDuplicate() {
        String helper = "donor456";

        // Add donation reward twice
        rewardsService.addFirstDonationReward(helper);
        rewardsService.addFirstDonationReward(helper);

        List<Rewards> rewards = rewardsService.getRewards(helper);
        assertEquals(1, rewards.size(), "Should not add duplicate donation reward");
    }

    @Test
    public void testUserCanHaveMultipleDifferentRewards() {
        String helper = "multiRewardUser";

        // Add both purchase and donation rewards
        rewardsService.recordPurchase(helper);
        rewardsService.addFirstDonationReward(helper);

        List<Rewards> rewards = rewardsService.getRewards(helper);
        assertEquals(2, rewards.size(), "User should have two different rewards");

        Set<String> rewardTitles = new HashSet<>();
        for (Rewards r : rewards) {
            rewardTitles.add(r.getTitle());
        }

        assertTrue(rewardTitles.contains("First Purchase"));
        assertTrue(rewardTitles.contains("First Donation Reward"));
    }

    @Test
    public void testGetRewards_returnsEmptyListIfNoneExist() {
        String helper = "noRewardsUser";
        List<Rewards> rewards = rewardsService.getRewards(helper);

        assertNotNull(rewards);
        assertTrue(rewards.isEmpty(), "Should return an empty list for user with no rewards");
    }
}
