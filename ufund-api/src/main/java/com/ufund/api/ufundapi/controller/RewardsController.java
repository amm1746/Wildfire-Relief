package com.ufund.api.ufundapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ufund.api.ufundapi.model.Rewards;

@RestController
@RequestMapping("/api")
public class RewardsController {

    private final RewardsService rewardsService;

    @Autowired
    public RewardsController(RewardsService rewardsService) {
        this.rewardsService = rewardsService;
    }

    @GetMapping("/rewards")
    public List<Rewards> getRewards(@RequestParam String helper) {
        return rewardsService.getRewards(helper);
    }
}
