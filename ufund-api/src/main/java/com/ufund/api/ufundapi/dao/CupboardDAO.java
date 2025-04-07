package com.ufund.api.ufundapi.dao;
import java.io.IOException;
import java.util.List;
import com.ufund.api.ufundapi.model.Need;

/**
 * Interface for managing needs in the cupboard.
 *
 * @author Alexandra Mantagas
 */

 public interface CupboardDAO{
   /**
    * Gets a list of needs that match given name.
    *
    * @param name The name of need (partial or full).
    * @return A list of needs that match.
    */

    List<Need> needByName(String name);

    /**
    * Gets a need by name.
    *
    * @param name The name of need.
    * @return The matching need, otherwise null.
     * @throws IOException 
    */

    Need getNeed(String name) throws IOException;

   /**
    * Creates a new need.
    *
    * @param name The name of need being created.
    * @return The created need.
    * @throws IOException If an error occurs.
    */

    Need createNeed(Need need) throws IOException;

   /**
    * Gets all needs in cupboard.
    *
    * @return A list of needs. 
    */

    List<Need> getAllNeeds();

   /**
    * Deletes a need by name. 
    *
    * @param name The name of need being deleted.
 * @throws IOException 
    */

    void deleteNeed(String name) throws IOException;

   /**
    * Checks if a need exists or not.
    *
    * @param name The name of need.
    * @return True if the need exists, otherwise false. 
    */

    boolean needExists(String name);

   /**
    * Updates an existing need.
    *
    * @param name The name of need being updated.
    * @param updated The updated need.
    * @return The updated need.
 * @throws IOException 
    */
    
    Need updateNeed(String name, Need updated) throws IOException;
 }

