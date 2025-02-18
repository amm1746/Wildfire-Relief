package com.ufund.api.ufundapi.controller;

import com.ufund.api.ufundapi.dao.CupboardDAO;
import com.ufund.api.ufundapi.model.Need;


public class CupboardController {

    private final CupboardDAO cupboardDAO;

    public CupboardController(CupboardDAO cupboardDAO) {
        this.cupboardDAO = cupboardDAO;
    }

    public String updateNeed( String name, Need updatedNeed) {
        if (!cupboardDAO.needExists(name)) {
            return "Need not found";
        }

        Need updated = cupboardDAO.updateNeed(name, updatedNeed);
        return (updated != null) ? "Need updated successfully" : "Failed to update need";
    }
}
