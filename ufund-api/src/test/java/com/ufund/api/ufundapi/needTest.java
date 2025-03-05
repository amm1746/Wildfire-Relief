package com.ufund.api.ufundapi;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.ufund.api.ufundapi.model.Need;

/**
 * JUnit Tests for Need.java
 * 
 * @author Eric Zheng Wu
 */
public class needTest {
    
    private Need need;
    
    @BeforeEach
    public void setup() {
        need = new Need("Food", 15.50, 3, "Essential");
    }
    
    @Test
    public void testGetName() {
        assertEquals("Food", need.getName());
    }
    
    @Test
    public void testSetName() {
        need.setName("Water");
        assertEquals("Water", need.getName());
    }
    
    @Test
    public void testGetCost() {
        assertEquals(15.50, need.getCost());
    }
    
    @Test
    public void testSetCost() {
        need.setCost(20.00);
        assertEquals(20.00, need.getCost());
    }
    
    @Test
    public void testGetQuantity() {
        assertEquals(3, need.getQuantity());
    }
    
    @Test
    public void testSetQuantity() {
        need.setQuantity(5);
        assertEquals(5, need.getQuantity());
    }
    
    @Test
    public void testGetType() {
        assertEquals("Essential", need.getType());
    }
    
    @Test
    public void testSetType() {
        need.setType("Luxury");
        assertEquals("Luxury", need.getType());
    }
}
