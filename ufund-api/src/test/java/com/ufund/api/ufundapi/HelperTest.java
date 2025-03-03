package com.ufund.api.ufundapi;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import com.ufund.api.ufundapi.log.Helper;
import com.ufund.api.ufundapi.model.Need;

import com.ufund.api.ufundapi.dao.CupboardFileDAO;

import java.util.List;

public class HelperTest {

    private Helper helper;

    @Mock
    private CupboardFileDAO needsCupboard;

    @BeforeEach
    public void setup() {
        helper = new Helper(needsCupboard);
    }

    @Test
    public void testAddToFundingBasket() {
        Need need = new Need("Need1", 10.0, 1, "Type1");
        assertTrue(helper.addToFundingBasket(need));
        assertEquals(1, helper.getFundingBasket().size());
        assertEquals("Need1", helper.getFundingBasket().get(0).getName());
    }

    @Test
    public void testAddDuplicateNeed() {
        Need need = new Need("Need1", 10.0, 1, "Type1");
        helper.addToFundingBasket(need);
        assertFalse(helper.addToFundingBasket(need)); // Should not allow duplicate
        assertEquals(1, helper.getFundingBasket().size());
    }

    @Test
    public void testRemoveFromFundingBasket() {
        Need need = new Need("Need1", 10.0, 1, "Type1");
        helper.addToFundingBasket(need);
        assertTrue(helper.removeFromFundingBasket(need));
        assertEquals(0, helper.getFundingBasket().size());
    }

    @Test
    public void testRemoveNonExistentNeed() {
        Need need = new Need("Need1", 10.0, 1, "Type1");
        assertFalse(helper.removeFromFundingBasket(need)); // Should return false
    }

    @Test
    public void testGetFundingBasket() {
        Need need1 = new Need("Need1", 10.0, 1, "Type1");
        Need need2 = new Need("Need2", 20.0, 2, "Type2");
        helper.addToFundingBasket(need1);
        helper.addToFundingBasket(need2);

        List<Need> basket = helper.getFundingBasket();
        assertEquals(2, basket.size());
        assertTrue(basket.contains(need1));
        assertTrue(basket.contains(need2));
    }
}
