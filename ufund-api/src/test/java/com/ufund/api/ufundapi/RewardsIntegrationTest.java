package com.ufund.api.ufundapi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import com.ufund.api.ufundapi.controller.BasketController;
import com.ufund.api.ufundapi.dao.CupboardDAO;
import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.RewardsService;
import com.ufund.api.ufundapi.model.Rewards;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class RewardsIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RewardsService rewardsService; // Mock the RewardsService

    @Test
    public void testRewardsAfterCheckout() throws Exception {
        assertNotNull(rewardsService);
        // Mock behavior of rewardsService for a specific helper
        List<Rewards> mockRewards = Collections.singletonList(new Rewards("First Purchase", "Thanks for funding your first need!"));
        when(rewardsService.getRewards("helper1")).thenReturn(mockRewards);

        // Make sure the mock works when calling the /api/rewards endpoint
        mockMvc.perform(get("/api/rewards")
                .param("helper", "helper1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("First Purchase"))
                .andExpect(jsonPath("$[0].message").value("Thanks for funding your first need!"));
    }
}
