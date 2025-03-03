package com.ufund.api.ufundapi.log;

import com.ufund.api.ufundapi.model.Need;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper class to manage the funding basket.
 * 
 * @author Sophia Le
 */
public class Helper {
    private List<Need> fundingBasket;

    /**
     * Constructor to initialize the funding basket.
     */
    public Helper() {
        this.fundingBasket = new ArrayList<>();
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
}
