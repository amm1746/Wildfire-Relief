package com.ufund.api.ufundapi.controller;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ufund.api.ufundapi.dao.CupboardDAO;
import com.ufund.api.ufundapi.model.Need;

/**
 * Controller for managing cupboard-related operations such
 * as CRUD operations for needs
 * 
 * @author Sophia Le
 * @author Alexandra Mantagas
 */

 //restcontroller marks the class as a controller to handle HTTP requests
 //requestmapping sets base url 
 @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
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
    @PutMapping("/need/{name}")
    public ResponseEntity<String> updateNeed(@PathVariable String name, @RequestBody Need updatedNeed) {
        if (!cupboardDAO.needExists(name)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Need not found");
        }

        Need updated = cupboardDAO.updateNeed(name, updatedNeed);
        return (updated != null) ? ResponseEntity.ok("Need updated successfully") : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update need");
    }
    
    /**
     * Gets the entire cupboard
     * 
     * @return a list of all needs in the cupboard, empty list
     * otherwise
     */
    @GetMapping("/needs")
    public ResponseEntity<List<Need>> getCupboard() {
        List<Need> needs = cupboardDAO.getAllNeeds();
        return ResponseEntity.ok(needs);
    }

    /**
     * Creates a new need in the cupboard.
     * 
     * @param need The need being created.
     * @return The created need if successful, or a conflict status if it already exists.
     */
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

    /**
     * Gets a specific need by its name.
     * 
     * @param name The name of the need.
     * @return The need if found, otherwise a not found status.
     */
    @GetMapping("/need/{name}")
    public ResponseEntity<Need> getNeedByName(@PathVariable String name){
        Need need = cupboardDAO.getNeed(name);
        return(need != null) ? ResponseEntity.ok(need) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();

    }

    /**
     * Deletes a need by its name.
     * 
     * @param name The name of the need being deleted.
     * @return A message saying if it was deleted successfully or not.
     */
    @DeleteMapping("/need/{name}")
    public ResponseEntity<String> deleteNeed(@PathVariable String name){
        if(!cupboardDAO.needExists(name)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Need not found");
        }
        cupboardDAO.deleteNeed(name);
        return ResponseEntity.ok("Need deleted successfully");
    }

    /**
     * Searches for needs based on partial or full name match.
     * 
     * @param name The name (partial or full) of need being searched for.
     * @return A list of needs matching the name.
     */
    @GetMapping("/needs/search")
    public ResponseEntity<?> searchNeeds(@RequestParam String name){
        List<Need> needs = cupboardDAO.getAllNeeds();
        List<Need> searchedNeeds = new ArrayList<>();
        for(Need need : needs){
            if(need.getName().toLowerCase().contains(name.toLowerCase())){
                searchedNeeds.add(need);
            }
        }

        if (searchedNeeds.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "No needs found matching your search."));
        }

        return ResponseEntity.ok(searchedNeeds);
    }
}

