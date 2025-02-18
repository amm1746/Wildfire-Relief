/**
 * Interface for cupboard.
 *
 * @author Alexandra Mantagas
 */

 package com.ufund.api.ufundapi.dao;
 import java.io.IOException;
 import java.util.List;

 import com.ufund.api.ufundapi.model.Need;

 public interface CupboardDAO{
    Need needByName(String name);
    Need createNeed(Need need) throws IOException;
    List<Need> getAllNeeds();
    void deleteNeed(String name);
    boolean needExists(String name);
    Need updateNeed(String name, Need updated);
 }

