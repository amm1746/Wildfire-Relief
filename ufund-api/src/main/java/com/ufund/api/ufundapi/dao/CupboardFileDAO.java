package com.ufund.api.ufundapi.dao;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.Need;

/**
 * Implementation of CupboardDAO.java
 *
 * @author Alexandra Mantagas
 * @author Evan Lin
 */

@Component
public class CupboardFileDAO implements CupboardDAO {
    private final ObjectMapper objectMapper;
    private final String filename;
    private List<Need> needs;

    /**
     * Constructs CupboardFileDAO with an empty list of needs.
     */
    public CupboardFileDAO(@Value("${needs.file}") String filename, 
                          ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();
    }

    /**
     * Creates and adds a need to the cupboard.
     *
     * @param need The need being added.
     * @return The created need, or null if it already exists.
     */
    @Override
    public Need createNeed(Need need) throws IOException {
        if (needExists(need.getName())) return null;
        needs.add(need);
        save();
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
        try {
        needs.removeIf(need -> need.getName().equalsIgnoreCase(name));
        save();
        } catch (IOException e) {
            // Log error or throw as unchecked exception
            throw new RuntimeException("Failed to delete needs", e);
        }

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
        try {
            for (int i = 0; i < needs.size(); i++) {
                if (needs.get(i).getName().equalsIgnoreCase(name)) {
                    needs.set(i, updated);
                    save();
                    return updated;
                }
            }
            return null;
        } catch (IOException e) {
            throw new RuntimeException("Failed to update needs", e);
    }
    }
    /**
     * Load needs from JSON file
     * 
     * @throws IOException
     */
    private void load() throws IOException {
        File file = new File(filename);
        needs = file.exists() 
            ? new ArrayList<>(Arrays.asList(objectMapper.readValue(file, Need[].class)))
            : new ArrayList<>();
    }
    /**
     * Save needs to JSON file
     * 
     * @throws IOException
     */
    private void save() throws IOException {
        objectMapper.writeValue(new File(filename), needs);
    }
}