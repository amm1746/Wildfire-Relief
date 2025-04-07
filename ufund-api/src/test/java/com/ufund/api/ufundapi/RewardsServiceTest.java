package com.ufund.api.ufundapi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import com.ufund.api.ufundapi.model.Rewards;
import com.ufund.api.ufundapi.model.RewardsService;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit tests for the Rewards system
 * @author Sophia Le
 */
@SpringBootTest
public class RewardsServiceTest {

    @Mock
    private Map<String, Rewards> mockRewards;

    private RewardsService rewardsService;

    @BeforeEach
    public void setUp() {
        // Initialize mocks and inject into RewardsService
        MockitoAnnotations.openMocks(this);
        rewardsService = new RewardsService(null, mockRewards);  // Ensure RewardsService is initialized with the mock
    }

    @Test
    public void testRecordPurchase_addsFirstPurchaseReward() {
        String helper = "user123";

        // Mock the rewards map to return null (simulate no existing rewards)
        when(mockRewards.get(helper)).thenReturn(null);

        // Simulate the purchase by calling recordPurchase
        rewardsService.recordPurchase(helper);

        // Verify that the first purchase reward is added to the rewards map
        verify(mockRewards).put(eq(helper), argThat(reward -> 
            reward.getTitle().equals("First Purchase") && 
            reward.getDescription().equals("You made your first purchase!")
        ));
    }

    @Test
    public void testAddFirstDonationReward_addsFirstDonationReward_whenNoRewardExists() {
        String helper = "user123";
        
        // Simulate no reward for the user
        when(mockRewards.get(helper)).thenReturn(null);

        // Call the method to add the first donation reward
        rewardsService.addFirstDonationReward(helper);

        verify(mockRewards).put(eq(helper), argThat(reward ->
        "First Donation Reward".equals(reward.getTitle()) &&
        "You earned a special first donation reward!".equals(reward.getDescription())
    ));
    }
    

    @Test
    public void testAddFirstDonationReward_doesNotAddAgain_whenAlreadyFirstDonationReward() {
        String helper = "user123";

        // Simulate that the user already has a "First Donation Reward"
        Rewards existingReward = new Rewards("First Donation Reward", "You earned a special first donation reward!");
        when(mockRewards.get(helper)).thenReturn(existingReward);

        // Call the method to add the first donation reward
        rewardsService.addFirstDonationReward(helper);

        // Verify that the reward was not added again
        verify(mockRewards, times(0)).put(eq(helper), any());  // It should not try to put a new reward
    }
    @Test
    public void testGetRewards_returnsCorrectRewards() {
        String helper = "user123";

        // Create a list of rewards for the user
        List<Rewards> rewardsList = new ArrayList<>();
        Rewards firstPurchaseReward = new Rewards("First Purchase", "You made your first purchase!");
        rewardsList.add(firstPurchaseReward);
        
        // Mock the rewards map to return a single Rewards object (not a List)
        // Since rewardsService.getRewards returns a List<Rewards>, we mock get() accordingly.
        when(mockRewards.get(helper)).thenReturn(firstPurchaseReward);  // Simulate getting one reward

        // Call the getRewards method
        List<Rewards> userRewards = rewardsService.getRewards(helper);

        // Verify that the returned reward is correct
        assertNotNull(userRewards, "Rewards list should not be null");
        assertEquals(1, userRewards.size(), "Rewards list should have one item");
        assertEquals("First Purchase", userRewards.get(0).getTitle(), "Reward title should be 'First Purchase'");
        assertEquals("You made your first purchase!", userRewards.get(0).getDescription(), "Reward description should match");
    }

}
