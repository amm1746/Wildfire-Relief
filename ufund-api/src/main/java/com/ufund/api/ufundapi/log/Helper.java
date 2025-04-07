package com.ufund.api.ufundapi.log;

import com.ufund.api.ufundapi.model.Rewards;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.ufund.api.ufundapi.controller.RewardsService;
import com.ufund.api.ufundapi.dao.CupboardDAO;
import com.ufund.api.ufundapi.model.Need;

/**
 * Helper class to manage the funding basket.
 * 
 * @author Sophia Le
 * @author Eric Zheng Wu
 */
public class Helper {
    private final List<Need> fundingBasket;
    private final RewardsService rewardsService;
    @SuppressWarnings("FieldMayBeFinal")
    private CupboardDAO cupboardDAO;

    /**
     * Constructor to initialize the funding basket.
     */
    public Helper(CupboardDAO cupboardDAO, RewardsService rewardsService) {
        this.fundingBasket = new ArrayList<>();
        this.cupboardDAO = cupboardDAO;
        this.rewardsService = rewardsService;
    }

    /**
     * search for a specific need
     * @param name name of the need
     * @return the need that matches the name
     * @throws IOException 
     */
    public Need searchNeed(String name) throws IOException {
        Need need = cupboardDAO.getNeed(name);
        return need;
    }

    /**
     * Adds a need to the funding basket.
     * 
     * @param need The need to add.
     * @return true if added successfully, false if it already exists.
     */
    public boolean addToFundingBasket(Need need) {
        if (!fundingBasket.contains(need)) {
            fundingBasket.add(need);
            rewardsService.recordPurchase("Helper");
            return true;
        }
        return false; // Need already in the basket
    }

    /**
     * Removes a need from the funding basket.
     * 
     * @param need The need to remove.
     * @return true if removed successfully, false if it was not in the basket.
     */
    public boolean removeFromFundingBasket(Need need) {
        return fundingBasket.remove(need);
    }

    /**
     * Gets the current list of needs in the funding basket.
     * 
     * @return List of needs in the basket.
     */
    public List<Need> getFundingBasket() {
        return new ArrayList<>(fundingBasket);
    }

    public void checkout() throws IOException {
      for(Need need: fundingBasket) {
        cupboardDAO.deleteNeed(need.getName());
      }
      fundingBasket.clear();
    }
  
    public List<Need> viewAllNeeds() {
      return cupboardDAO.getAllNeeds();
    }

    /**
     * Retrieves all the Helper's earned rewards
     * @return a list of all the rewards a Helper has achieved
     */
    public List<Rewards> getRewards() {
        return rewardsService.getRewards("Helper");
    }
}
