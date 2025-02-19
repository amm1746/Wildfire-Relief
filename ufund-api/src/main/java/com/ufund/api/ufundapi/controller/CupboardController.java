package com.ufund.api.ufundapi.controller;
import java.util.*;
import org.springframework.web.bind.annotation.*;
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
 * @author Alexandra Mantagas
 */

 //restcontroller marks the class as a controller to handle HTTP requests
 //requestmapping sets base url 
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

    @GetMapping("/need/{name}")
    public ResponseEntity<Need> getNeedByName(@PathVariable String name){
        Need need = cupboardDAO.getNeed(name);
        return(need != null) ? ResponseEntity.ok(need) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();

    }

    @DeleteMapping("/need/{name}")
    public ResponseEntity<String> deleteNeed(@PathVariable String name){
        if(!cupboardDAO.needExists(name)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Need not found");
        }
        cupboardDAO.deleteNeed(name);
        return ResponseEntity.ok("Need deleted successfully");
    }

    @GetMapping("/needs/search")
    public ResponseEntity<List<Need>> searchNeeds(@RequestParam String name){
        List<Need> needs = cupboardDAO.getAllNeeds();
        List<Need> searchedNeeds = new ArrayList<>();
        for(Need need : needs){
            if(need.getName().toLowerCase().contains(name.toLowerCase())){
                searchedNeeds.add(need);
            }
        }
        return ResponseEntity.ok(searchedNeeds);
    }
}
