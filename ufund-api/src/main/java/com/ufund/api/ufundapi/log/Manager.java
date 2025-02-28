package com.ufund.api.ufundapi.log;
import java.io.IOException;
import java.util.List;

import com.ufund.api.ufundapi.dao.CupboardDAO;
import com.ufund.api.ufundapi.model.Need;

/**
 * Manager class for handling operations related to needs
 * in the cupboard. Implements logic for modifying stored
 * needs.
 *
 * @author Evan Lin
 * @author Sophia Le
 */

public class Manager {
    // initializes an empty cupboard
    private static CupboardDAO cupboardDAO = null;
    
    public Manager(CupboardDAO cupboardDAO) {
            this.cupboardDAO = cupboardDAO;
    }

    /**
     * Retrieves all stored needs in the cupboard
     * @return a list of all needs
     */
    public List<Need> viewAllNeeds() {
        return cupboardDAO.getAllNeeds();
    }

    /**
     * Adds a new need to the cupboard.
     * @param need the need object to be added
     * @return the created need if successful, otherwise null if the need already exists
     * @throws IOException if an error occurs while saving
     */
    public Need addNeed(Need need) throws IOException {
        return cupboardDAO.createNeed(need);
    }

    /**
     * Updates an existing need in the cupboard
     * @param name the name of the need to be updated
     * @param updated the updated need object
     * @return the updated need if successful, otherwise null if not found
     */
    public Need updateNeed(String name, Need updated) {
        return cupboardDAO.updateNeed(name, updated);
    }
    /**
     * Deletes a need from the cupboard by its name
     * @param name the name of the need to be deleted
     * @return True if the deletion was successful, otherwise False
     */
    public boolean deleteNeed(String name) {
        if(cupboardDAO.needExists(name)) {
            cupboardDAO.deleteNeed(name);
            return true;
        }
        return false;
    }
    // Need create(String name, double cost, int quantity, String type);

}