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
        testNeed = new Need("name", 10.0, 5, "Supply");
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
}