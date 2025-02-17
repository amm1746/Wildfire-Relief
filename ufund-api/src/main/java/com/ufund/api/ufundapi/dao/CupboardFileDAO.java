/**
 * Implementation of CupboardDAO.java
 *
 * @author Alexandra Mantagas
 */

 package com.ufund.api.ufundapi.dao;

 import com.ufund.api.ufundapi.model.Need;
 import java.util.*;

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
 }