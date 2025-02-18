/**
 * Implementation of CupboardDAO.java
 *
 * @author Alexandra Mantagas
 */

 package com.ufund.api.ufundapi.dao;

 import java.util.ArrayList;
 import java.util.List;
 import com.fasterxml.jackson.databind.ObjectMapper;
 import com.ufund.api.ufundapi.model.Need;
 import java.io.IOException;
 import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

 @Component
 public class CupboardFileDAO implements CupboardDAO{
    List<Need> needs = new ArrayList<>();
    private ObjectMapper objectMapper;
    private String filename;

    //need file data access object 
    public CupboardFileDAO(@Value("${cupboard.file}") String filename, ObjectMapper objectMapper) throws IOException{
        this.filename = filename;
        this.objectMapper = objectMapper;
        this.needs = new ArrayList<>();
        load();
    }

    //saves the list of need objects to a file in JSON format
    private boolean save() throws IOException{
        objectMapper.writeValue(new File(filename), needs);
        return true;
    }

    //loads previously saved need objects from json file into memory 
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

    //creates need 
    @Override
    public Need createNeed(Need need) throws IOException{
        if(needExists(need.getName())){
            return null;
        }
        needs.add(need);
        save();
        return need;
    }
    @Override 
    public Need needByName(String name){
        return null;
    }
    @Override
    public List<Need> getAllNeeds(){
        return needs;
    }
    @Override
    public void deleteNeed(String name){

    }

    //whether need exists or not 
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