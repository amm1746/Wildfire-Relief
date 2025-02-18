/**
 * Implementation of CupboardDAO.java
 *
 * @author Alexandra Mantagas
 */

 package com.ufund.api.ufundapi.dao;

 import java.util.ArrayList;
 import java.util.List;

import com.ufund.api.ufundapi.model.Need;

 public class CupboardFileDAO implements CupboardDAO{
    List<Need> needs = new ArrayList<>();
    @Override
    public Need createNeed(Need need){
        if(needExists(need.getName())){
            return null;
        }
        else{
            needs.add(need);
            return need; 
        }
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
    @Override
    public boolean needExists(String name){
        return false;
    }

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