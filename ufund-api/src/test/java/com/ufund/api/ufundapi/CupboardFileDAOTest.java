package com.ufund.api.ufundapi;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
        cupboardFileDAO = new CupboardFileDAO("test_cupboard.json", new ObjectMapper());
        testNeed = new Need("Food", 15.50, 3, "Essential");
    }

    @Test
    public void testCreateNeed() throws IOException {
        // Act
        Need createdNeed = cupboardFileDAO.createNeed(testNeed);

        // Assert
        assertNotNull(createdNeed);
        assertEquals(testNeed.getName(), createdNeed.getName());
    }

    @Test
    public void testGetNeed() throws IOException {
        // Arrange
        cupboardFileDAO.createNeed(testNeed);

        // Act
        Need retrievedNeed = cupboardFileDAO.getNeed("Food");

        // Assert
        assertNotNull(retrievedNeed);
        assertEquals(testNeed.getName(), retrievedNeed.getName());
    }

    @Test
    public void testDeleteNeed() throws IOException {
        // Arrange
        cupboardFileDAO.createNeed(testNeed);

        // Act
        cupboardFileDAO.deleteNeed("Food");

        // Assert
        assertNull(cupboardFileDAO.getNeed("Food"));
    }

    @Test
    public void testUpdateNeed() throws IOException {
        // Arrange
        cupboardFileDAO.createNeed(testNeed);
        Need updatedNeed = new Need("Food", 20.00, 5, "Luxury");

        // Act
        Need result = cupboardFileDAO.updateNeed("Food", updatedNeed);

        // Assert
        assertNotNull(result);
        assertEquals(updatedNeed.getCost(), result.getCost());
        assertEquals(updatedNeed.getQuantity(), result.getQuantity());
    }

    @Test
    public void testNeedExists() throws IOException {
        // Arrange
        cupboardFileDAO.createNeed(testNeed);

        // Act
        boolean exists = cupboardFileDAO.needExists("Food");

        // Assert
        assertTrue(exists);
    }
}