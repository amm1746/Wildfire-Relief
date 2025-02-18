package com.ufund.api.ufundapi.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ufund.api.ufundapi.dao.CupboardDAO;
import com.ufund.api.ufundapi.model.Need;
import java.io.IOException;

/**
 * Controller for managing cupboard-related operations such
 * as CRUD operations for needs
 * 
 * @author Sophia Le
 */

 @RestController
 @RequestMapping("/cupboard")
public class CupboardController {

    private final CupboardDAO cupboardDAO;

    /**
     * Constructor to initialize CupboardController with a CupboardDAO instance
     * 
     * @param cupboardDAO the DAO instance for cupboard operations
     */
    public CupboardController(CupboardDAO cupboardDAO) {
        this.cupboardDAO = cupboardDAO;
    }

    /**
     * Endpoint to update an existing need
     * 
     * @param name the name of the need to update
     * @param updatedNeed the updated need object
     * @return a message indicating the result of the update
     */
    public String updateNeed( String name, Need updatedNeed) {
        if (!cupboardDAO.needExists(name)) {
            return "Need not found";
        }

        Need updated = cupboardDAO.updateNeed(name, updatedNeed);
        return (updated != null) ? "Need updated successfully" : "Failed to update need";
    }
    
    /**
     * Gets the entire cupboard
     * 
     * @return a list of all needs in the cupboard, empty list
     * otherwise
     */
    public List<Need> getCupboard() {
        List<Need> needs = cupboardDAO.getAllNeeds();
        return needs;
    }

    //accepts need object from request body, checks if need exists, creates need if doesnt exist and returns 201 CREATED, returns 409 Conflict if need exists 
    @PostMapping("/need")
    public ResponseEntity<Need> createNeed(@RequestBody Need need){
        try{
            if(cupboardDAO.needExists(need.getName())){
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            Need createdNeed = cupboardDAO.createNeed(need);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdNeed);
        }
        catch(IOException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
