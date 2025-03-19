package com.ufund.api.ufundapi;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ufund.api.ufundapi.dao.CupboardFileDAO;
import com.ufund.api.ufundapi.model.Need;

/**
 * JUnit Tests for CupboardFileDAO.java
 * 
 * @author Evan Lin
 * @author Eric Zheng Wu
 */
public class CupboardFileDAOTest {

    private CupboardFileDAO cupboardFileDAO;
    private Need testNeed;

    @BeforeEach
    public void setup() throws IOException {
        // Initialize the DAO with a test file and a simple ObjectMapper
        cupboardFileDAO = new CupboardFileDAO();
        testNeed = new Need("name", 20.0, 5, "type1");
    }

    @Test
    public void testCreateNeed() throws IOException {
        cupboardFileDAO.deleteNeed("name");
        Need createdNeed = cupboardFileDAO.createNeed(testNeed);

        assertNotNull(createdNeed);
        assertEquals(testNeed.getName(), createdNeed.getName());
    }

    @Test
    public void testGetNeed() throws IOException {
        cupboardFileDAO.createNeed(testNeed);
        Need retrievedNeed = cupboardFileDAO.getNeed("name");
        assertNotNull(retrievedNeed);
        assertEquals(testNeed.getName(), retrievedNeed.getName());
    }

    @Test
    public void testDeleteNeed() throws IOException {
        cupboardFileDAO.createNeed(testNeed);
        cupboardFileDAO.deleteNeed("name");

        assertNull(cupboardFileDAO.getNeed("name"));
    }

    @Test
    public void testUpdateNeed() throws IOException {
        cupboardFileDAO.createNeed(testNeed);
        Need updatedNeed = new Need("nam", 15.5, 3, "type2");
        Need result = cupboardFileDAO.updateNeed("name", updatedNeed);

        assertNotNull(result);
        assertEquals(updatedNeed.getCost(), result.getCost());
        assertEquals(updatedNeed.getQuantity(), result.getQuantity());
    }

    @Test
    public void testNeedExists() throws IOException {
        cupboardFileDAO.createNeed(testNeed);
        boolean exists = cupboardFileDAO.needExists("name");

        assertTrue(exists);
    }

    @Test
    public void testGetAllNeeds() throws IOException {
        cupboardFileDAO.createNeed(testNeed);
        assertEquals(1, cupboardFileDAO.getAllNeeds().size());
    }

    @Test
    public void testNeedByName() throws IOException {
        cupboardFileDAO.createNeed(new Need("apple", 10.0, 2, "typeA"));
        cupboardFileDAO.createNeed(new Need("application", 12.0, 3, "typeB"));
    
        assertEquals(2, cupboardFileDAO.needByName("app").size());
        assertEquals(1, cupboardFileDAO.needByName("apple").size());
    }

    @Test
    public void testCreateNeedAlreadyExists() throws IOException {
        cupboardFileDAO.createNeed(testNeed);
        Need duplicate = cupboardFileDAO.createNeed(testNeed);
        assertNull(duplicate); // Should return null if need already exists
    }

    @Test
    public void testUpdateNonExistentNeed() throws IOException {
        Need updatedNeed = new Need("newName", 30.0, 10, "typeNew");
        Need result = cupboardFileDAO.updateNeed("doesNotExist", updatedNeed);
        assertNull(result);
    }

    @Test
    public void testDeleteNonExistentNeed() throws IOException {
        cupboardFileDAO.deleteNeed("doesNotExist");
        assertNull(cupboardFileDAO.getNeed("doesNotExist"));
    }

}