package com.ufund.api.ufundapi.dao;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.Need;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.File;

/**
 * Implementation of CupboardDAO.java
 *
 * @author Alexandra Mantagas
 * @author Evan Lin
 */

 @Component
 public class CupboardFileDAO implements CupboardDAO{
    List<Need> needs = new ArrayList<>();
    private ObjectMapper objectMapper;
    private String filename;

    /**
     * Constructs CupboardFileDAO and loads existing needs.
     * 
     * @param filename File that stores need data.
     * @param objectMapper JSON object mapper
     * @throws IOException If an error occurs
     */ 

    public CupboardFileDAO(@Value("${cupboard.file}") String filename, ObjectMapper objectMapper) throws IOException{
        this.filename = filename;
        this.objectMapper = objectMapper;
        this.needs = new ArrayList<>();
        load();
    }

    /**
     * Saves the list of need objects to a file 
     * 
     * @return True if successful.
     * @throws IOException If an error occurs.
     */

    private boolean save() throws IOException{
        objectMapper.writeValue(new File(filename), needs);
        return true;
    }

    /**
     * Loads saved need objects into memory.
     * 
     * @return True if successful.
     * @throws IOException If an error occurs.
     */

    private boolean load() throws IOException{
        File file = new File(filename);
        if(!file.exists()){
            needs = new ArrayList<>();
            return true;
        }
        Need[] needArray = objectMapper.readValue(file, Need[].class);
        needs.clear();
        for(Need need : needArray){
            needs.add(need);
        }
        return true;
    }

    /**
     * Creates and adds a need to the cupboard.
     *
     * @param need The need being added.
     * @return The created need, or null if it already exists.
     * @throws IOException If an error occurs.
     */

    @Override
    public Need createNeed(Need need) throws IOException{
        if(needExists(need.getName())){
            return null;
        }
        needs.add(need);
        save();
        return need;
    }

    /**
     * Get a single need.
     * 
     * @param name The name of the need
     * @return The need object if exist
     */
    @Override 
    public Need getNeed(String name){
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
    public List<Need> getAllNeeds(){
        return needs;
    }

    /**
     * Deletes a need by its name from the cupboard.
     *
     * @param name The name of the need being deleted.
     */

    @Override
    public void deleteNeed(String name)
    {
        needs.removeIf(need -> need.getName().equalsIgnoreCase(name));
    }

    /**
     * Checks whether a need exists or not.
     *
     * @param name The name of the need.
     * @return True if the need exists, false if it does not.
     */

    @Override
    public boolean needExists(String name){
        for(Need need : needs){
            if(need.getName().equals(name)){
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
        for(int i = 0; i < needs.size(); i++) {
            if(needs.get(i).getName().equalsIgnoreCase(name)) {
                needs.set(i, updated);
                return updated;
            }
        }
        return null; // need not found
    }
 }