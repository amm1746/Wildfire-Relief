package com.ufund.api.ufundapi;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.dao.BasketFileDAO;
import com.ufund.api.ufundapi.model.Need;

/**
 * JUnit Tests for BasketFileDAO.java
 * 
 * @author Evan Lin 
 */

public class BasketFileDAOTest {

    private BasketFileDAO basketFileDAO;
    private Need testNeed1;
    private Need testNeed2;

    @BeforeEach
    public void setup() throws IOException {
        Files.write(Paths.get("test-baskets.json"), "{}".getBytes());
        ObjectMapper objectMapper = new ObjectMapper();
        basketFileDAO = new BasketFileDAO("test-baskets.json", objectMapper);
        testNeed1 = new Need("Food", 10.99, 1, "Basic need");
        testNeed2 = new Need("Shelter", 100.50, 1, "Basic need");
    }
    
    @Test
    public void testGetBasket() throws IOException {
        List<Need> needs = new ArrayList<>();
        needs.add(testNeed1);
        basketFileDAO.saveBasket("testUser", needs);

        List<Need> retrievedBasket = basketFileDAO.getBasket("testUser");
        assertNotNull(retrievedBasket);
        assertEquals(1, retrievedBasket.size());
        assertEquals(testNeed1.getName(), retrievedBasket.get(0).getName());
    }

    @Test
    public void testGetBasketNoUser() throws IOException {
        List<Need> retrievedBasket = basketFileDAO.getBasket("noUser");
        assertNotNull(retrievedBasket);
        assertTrue(retrievedBasket.isEmpty());
    }

    @Test
    public void testSaveBasket() throws IOException {
        List<Need> needs = new ArrayList<>();
        needs.add(testNeed1);
        needs.add(testNeed2);

        basketFileDAO.saveBasket("testUser", needs);

        List<Need> retrievedBasket = basketFileDAO.getBasket("testUser");
        assertNotNull(retrievedBasket);
        assertEquals(2, retrievedBasket.size());
    }

    @Test
    public void testSaveBasketEmpty() throws IOException {
        List<Need> emptyNeeds = new ArrayList<>();
        basketFileDAO.saveBasket("testUser", emptyNeeds);

        List<Need> retrievedBasket = basketFileDAO.getBasket("testUser");
        assertNotNull(retrievedBasket);
        assertTrue(retrievedBasket.isEmpty());
    }

    @Test
    public void testMultipleUsers() throws IOException {
        List<Need> user1Needs = new ArrayList<>();
        user1Needs.add(testNeed1);
        basketFileDAO.saveBasket("user1", user1Needs);

        List<Need> user2Needs = new ArrayList<>();
        user2Needs.add(testNeed2);
        basketFileDAO.saveBasket("user2", user2Needs);

        List<Need> retrieved1 = basketFileDAO.getBasket("user1");
        List<Need> retrieved2 = basketFileDAO.getBasket("user2");

        assertEquals(1, retrieved1.size());
        assertEquals(testNeed1.getName(), retrieved1.get(0).getName());
        assertEquals(1, retrieved2.size());
        assertEquals(testNeed2.getName(), retrieved2.get(0).getName());
    }

    @Test
    public void testLoadMissingFile() throws IOException {
        Files.deleteIfExists(Paths.get("test-baskets.json"));
        ObjectMapper mapper = new ObjectMapper();
        BasketFileDAO freshDAO = new BasketFileDAO("test-baskets.json", mapper);

        assertTrue(freshDAO.getBasket("anyUser").isEmpty());
    }
}