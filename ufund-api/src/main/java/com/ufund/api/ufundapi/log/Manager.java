package com.ufund.api.ufundapi.log;
import java.io.IOException;
import java.util.List;

import com.ufund.api.ufundapi.dao.CupboardDAO;
import com.ufund.api.ufundapi.model.Need;

/**
 * Interface for the manager.
 *
 * @author Evan Lin
 */

public class Manager {
    public static CupboardDAO cupboardDAO = null;
    
    public Manager(CupboardDAO cupboardDAO) {
            this.cupboardDAO = cupboardDAO;
    }

    public List<Need> viewAllNeeds() {
        return cupboardDAO.getAllNeeds();
    }

    public Need addNeed(Need need) throws IOException {
        return cupboardDAO.createNeed(need);
    }
    // Need create(String name, double cost, int quantity, String type);

}