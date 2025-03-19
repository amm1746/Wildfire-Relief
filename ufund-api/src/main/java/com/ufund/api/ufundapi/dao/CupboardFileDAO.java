package com.ufund.api.ufundapi.dao;

import java.util.ArrayList;
import java.util.List;
import com.ufund.api.ufundapi.model.Need;
import org.springframework.stereotype.Component;

/**
 * Implementation of CupboardDAO.java
 *
 * @author Alexandra Mantagas
 * @author Evan Lin
 */

@Component
public class CupboardFileDAO implements CupboardDAO {
    List<Need> needs = new ArrayList<>();

    /**
     * Constructs CupboardFileDAO with an empty list of needs.
     */
    public CupboardFileDAO() {
        this.needs = new ArrayList<>();
    }

    /**
     * Creates and adds a need to the cupboard.
     *
     * @param need The need being added.
     * @return The created need, or null if it already exists.
     */
    @Override
    public Need createNeed(Need need) {
        if (needExists(need.getName())) {
            return null;
        }
        needs.add(need);
        return need;
    }

    /**
     * Get a single need.
     * 
     * @param name The name of the need
     * @return The need object if it exists, otherwise null.
     */
    @Override
    public Need getNeed(String name) {
        for (Need need : needs) {
            if (need.getName().equalsIgnoreCase(name)) {
                return need;
            }
        }
        return null;
    }

    /**
     * Searches for needs containing the given substring in their name.
     * 
     * @param substring The substring to search for
     * @return A list of needs whose names contain the given substring
     */
    @Override
    public List<Need> needByName(String substring) {
        List<Need> matchingNeeds = new ArrayList<>();
        for (Need need : needs) {
            if (need.getName().toLowerCase().contains(substring.toLowerCase())) {
                matchingNeeds.add(need);
            }
        }
        return matchingNeeds;
    }

    /**
     * Gets all needs in the cupboard.
     *
     * @return A list of all stored needs.
     */
    @Override
    public List<Need> getAllNeeds() {
        return needs;
    }

    /**
     * Deletes a need by its name from the cupboard.
     *
     * @param name The name of the need being deleted.
     */
    @Override
    public void deleteNeed(String name) {
        needs.removeIf(need -> need.getName().equalsIgnoreCase(name));
    }

    /**
     * Checks whether a need exists or not.
     *
     * @param name The name of the need.
     * @return True if the need exists, false if it does not.
     */
    @Override
    public boolean needExists(String name) {
        for (Need need : needs) {
            if (need.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Updates an existing need.
     * 
     * @param name The name of the need to be updated
     * @param updated The need object containing updated data
     * @return The updated need if successful, null otherwise
     */
    @Override
    public Need updateNeed(String name, Need updated) {
        for (int i = 0; i < needs.size(); i++) {
            if (needs.get(i).getName().equalsIgnoreCase(name)) {
                needs.set(i, updated);
                return updated;
            }
        }
        return null; // need not found
    }
}