package com.ufund.api.ufundapi.controller;

import java.util.List;

import com.ufund.api.ufundapi.dao.CupboardDAO;
import com.ufund.api.ufundapi.model.Need;

/**
 * Controller for managing cupboard-related operations such
 * as CRUD operations for needs
 * 
 * @author Sophia Le
 */
public class CupboardController {

    private final CupboardDAO cupboardDAO;

    /**
     * Constructor to initialize CupboardController with a CupboardDAO instance
     * 
     * @param cupboardDAO the DAO instance for cupboard operations
     */
    public CupboardController(CupboardDAO cupboardDAO) {
        this.cupboardDAO = cupboardDAO;
    }

    /**
     * Endpoint to update an existing need
     * 
     * @param name the name of the need to update
     * @param updatedNeed the updated need object
     * @return a message indicating the result of the update
     */
    public String updateNeed( String name, Need updatedNeed) {
        if (!cupboardDAO.needExists(name)) {
            return "Need not found";
        }

        Need updated = cupboardDAO.updateNeed(name, updatedNeed);
        return (updated != null) ? "Need updated successfully" : "Failed to update need";
    }
    
    public List<Need> getCupboard() {
        List<Need> needs = cupboardDAO.getAllNeeds();
        return needs;
    }
}
