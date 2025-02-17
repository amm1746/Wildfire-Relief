/**
 * Implementation of CupboardDAO.java
 *
 * @author Alexandra Mantagas
 */

 package com.ufund.api.ufundapi.dao;

 import main.java.com.ufund.api.ufundapi.model.Need;
 import java.util.*;
 import java.io.*;

 public class CupboardFileDAO implements CupboardDAO{
    public CupboardFileDAO(){
        loadFromFile();
    }

    @Override
    public Need createNeed(Need need){
        List<Need> needs = new ArrayList<>();
        if(needExists(need.getName())){
            return null;
        }
        else{
            needs.add(need);
            saveToFile();
            return need; 
        }
    }
 }