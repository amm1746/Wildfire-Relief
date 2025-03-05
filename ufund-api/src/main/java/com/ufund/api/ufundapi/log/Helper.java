package com.ufund.api.ufundapi.log;

import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.dao.CupboardDAO;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper class to manage the funding basket.
 * 
 * @author Sophia Le
 * @author Eric Zheng Wu
 */
public class Helper {
    private List<Need> fundingBasket;
    private static CupboardDAO cupboardDAO = null;

    /**
     * Constructor to initialize the funding basket.
     */
    public Helper(CupboardDAO cupboardDAO) {
        this.fundingBasket = new ArrayList<>();
        this.cupboardDAO = cupboardDAO;
    }

    /**
     * search for a specific need
     * @param name name of the need
     * @return the need that matches the name
     */
    public Need searchNeed(String name) {
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
