package com.ufund.api.ufundapi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.springframework.http.ResponseEntity;

import com.ufund.api.ufundapi.controller.CupboardController;
import com.ufund.api.ufundapi.dao.CupboardDAO;
import com.ufund.api.ufundapi.model.Need;

/**
 * JUnit Tests for CupboardControlllerTest.java
 * 
 * @author Alexandra Mantagas
 */

@SuppressWarnings("null")
public class CupboardControllerTest{
    
    private CupboardController cupboardController;
    private CupboardDAO mockCupboardDAO;

    @BeforeEach
    public void setup(){
        mockCupboardDAO = mock(CupboardDAO.class);
        cupboardController = new CupboardController(mockCupboardDAO);
    }

    @Test
    public void testGetCupboard() {
        Need test = new Need("Rent", 2.5, 10, "Monetary");
        when(mockCupboardDAO.getAllNeeds()).thenReturn(Arrays.asList(test));

        ResponseEntity<List<Need>> response = cupboardController.getCupboard();

        assertEquals(1, response.getBody().size());
        assertEquals("Rent", response.getBody().get(0).getName());
    }
    
    @Test
    public void testCreateNeed() throws IOException{
        Need test = new Need("Rent", 2.5, 10, "Monetary");
        when(mockCupboardDAO.needExists("Rent")).thenReturn(false);
        when(mockCupboardDAO.createNeed(test)).thenReturn(test);

        ResponseEntity<Need> response = cupboardController.createNeed(test);

        assertEquals(201, response.getStatusCodeValue());

        @SuppressWarnings("null")
        Need need = response.getBody(); 
        assertNotNull(need);
        assertEquals("Rent", need.getName());
    }

    @Test
    public void testCreateNeedConflict() throws IOException{
        Need test = new Need("Rent", 2.5, 10, "Monetary");
        when(mockCupboardDAO.needExists("Rent")).thenReturn(true);

        ResponseEntity<Need> response = cupboardController.createNeed(test);

        assertEquals(409, response.getStatusCodeValue()); 
    }

    @Test
    public void testUpdateNeed() {
        Need updatedNeed = new Need("Rent", 2.5, 10, "Monetary");
        when(mockCupboardDAO.needExists("Rent")).thenReturn(true);
        when(mockCupboardDAO.updateNeed("Rent", updatedNeed)).thenReturn(updatedNeed);

        ResponseEntity<String> response = cupboardController.updateNeed("Rent", updatedNeed);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Need updated successfully", response.getBody());
    }

    @Test
    public void testUpdateNeedNotFound() {
        Need updatedNeed = new Need("Rent", 2.5, 10, "Monetary");
        when(mockCupboardDAO.needExists("Rent")).thenReturn(false);

        ResponseEntity<String> response = cupboardController.updateNeed("Rent", updatedNeed);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Need not found", response.getBody());
    }

    @Test
    public void testDeleteNeed() {
        when(mockCupboardDAO.needExists("Rent")).thenReturn(true);
        doNothing().when(mockCupboardDAO).deleteNeed("Rent");

        ResponseEntity<String> response = cupboardController.deleteNeed("Rent");

        assertEquals(200, response.getStatusCodeValue()); 
        assertEquals("Need deleted successfully", response.getBody());
    }

    @Test
    public void testDeleteNeed_NotFound() {
        when(mockCupboardDAO.needExists("Rent")).thenReturn(false);

        ResponseEntity<String> response = cupboardController.deleteNeed("Rent");

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Need not found", response.getBody());
    }

    @Test
    public void testSearchNeeds() {
        List<Need> allNeeds = Arrays.asList(
                new Need("Rent", 2.5, 10, "Monetary"),
                new Need("Water Bottles", 5, 10, "Emergency")
        );
        when(mockCupboardDAO.getAllNeeds()).thenReturn(allNeeds);

        ResponseEntity<List<Need>> response = cupboardController.searchNeeds("Rent");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals("Rent", response.getBody().get(0).getName());
    }

    @Test
    public void testSearchNeeds_NotFound() {
        List<Need> allNeeds = new ArrayList<>();
        when(mockCupboardDAO.getAllNeeds()).thenReturn(allNeeds);

        ResponseEntity<List<Need>> response = cupboardController.searchNeeds("Food");

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    public void testCreateNeedIOException() throws IOException {
        Need test = new Need("Rent", 2.5, 10, "Monetary");
        when(mockCupboardDAO.needExists("Rent")).thenReturn(false);
        when(mockCupboardDAO.createNeed(test)).thenThrow(new IOException());

        ResponseEntity<Need> response = cupboardController.createNeed(test);

        assertEquals(500, response.getStatusCodeValue());
    }

    @Test
    public void testUpdateNeedFailure() {
        Need updatedNeed = new Need("Rent", 2.5, 10, "Monetary");
        when(mockCupboardDAO.needExists("Rent")).thenReturn(true);
        when(mockCupboardDAO.updateNeed("Rent", updatedNeed)).thenReturn(null);

        ResponseEntity<String> response = cupboardController.updateNeed("Rent", updatedNeed);

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("Failed to update need", response.getBody());
    }

    @Test
    public void testSearchNeedsPartialMatch() {
        List<Need> allNeeds = Arrays.asList(
                new Need("Rent Assistance", 2.5, 10, "Monetary"),
                new Need("Food Stamps", 5, 10, "Emergency")
        );
        when(mockCupboardDAO.getAllNeeds()).thenReturn(allNeeds);
    
        ResponseEntity<List<Need>> response = cupboardController.searchNeeds("Rent");
    
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals("Rent Assistance", response.getBody().get(0).getName());
    }

    @Test
    public void testGetNeedByName_Found() {
        Need testNeed = new Need("Rent", 2.5, 10, "Monetary");
        when(mockCupboardDAO.getNeed("Rent")).thenReturn(testNeed);
    
        ResponseEntity<Need> response = cupboardController.getNeedByName("Rent");
    
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Rent", response.getBody().getName());
    }

    @Test
    public void testGetNeedByName_NotFound() {
        when(mockCupboardDAO.getNeed("NonExistent")).thenReturn(null);

        ResponseEntity<Need> response = cupboardController.getNeedByName("NonExistent");

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

}