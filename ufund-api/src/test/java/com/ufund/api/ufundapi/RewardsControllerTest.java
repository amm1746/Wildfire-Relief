package com.ufund.api.ufundapi;

import com.ufund.api.ufundapi.controller.RewardsController;
import com.ufund.api.ufundapi.controller.RewardsService;
import com.ufund.api.ufundapi.model.Rewards;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class RewardsControllerTest {

    private RewardsService rewardsService;
    private RewardsController rewardsController;

    @BeforeEach
    public void setup() {
        rewardsService = mock(RewardsService.class);
        rewardsController = new RewardsController(rewardsService);
    }

    @Test
    public void testGetRewardsReturnsCorrectList() {
        List<Rewards> mockRewards = List.of(
            new Rewards("First Purchase", "Congrats on your first!"),
            new Rewards("Frequent Buyer", "You're a regular!")
        );

        when(rewardsService.getRewards("helper123")).thenReturn(mockRewards);

        List<Rewards> result = rewardsController.getRewards("helper123");

        assertEquals(2, result.size());
        assertEquals("First Purchase", result.get(0).getTitle());
        verify(rewardsService, times(1)).getRewards("helper123");
    }
}
