package com.ufund.api.ufundapi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.ufund.api.ufundapi.controller.BasketController;
import com.ufund.api.ufundapi.dao.BasketFileDAO;
import com.ufund.api.ufundapi.dao.CupboardDAO;
import com.ufund.api.ufundapi.dao.NotificationDAO;
import com.ufund.api.ufundapi.dao.UserDAO;
import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.RewardsService;

import jakarta.servlet.http.HttpSession;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)  
class BasketControllerTest {

    @Mock
    private CupboardDAO cupboardDAO;

    @Mock
    private HttpSession session;

    @Mock
    @SuppressWarnings("unused")
    private UserDAO userDAO;

    @Mock
    @SuppressWarnings("unused")
    private NotificationDAO notificationDAO;

    @Mock
    @SuppressWarnings("unused")
    private RewardsService rewardsService;

    @Mock
    @SuppressWarnings("unused")
    private BasketFileDAO basketFileDAO;

    @InjectMocks
    private BasketController basketController;

    @Test
    void testAddToBasket_Success() throws IOException {
        lenient().when(session.getAttribute("username")).thenReturn("helper");
        Need testNeed = new Need("Blankets", 10.0, 5, "Supply");
        List<Need> basket = new ArrayList<>();
        
        when(cupboardDAO.getNeed("Blankets")).thenReturn(testNeed);
        when(session.getAttribute("basket")).thenReturn(basket);

        Map<String, String> response = basketController.addToBasket(testNeed, session);
        assertEquals("Added to basket", response.get("message"));
    }

    @Test
    void testAddToBasket_NeedNotFound() throws IOException {
    lenient().when(session.getAttribute("username")).thenReturn("helper");
    Need testNeed = new Need("Blankets", 10.0, 5, "Supply");

    when(cupboardDAO.getNeed("Blankets")).thenReturn(null);

    ResponseStatusException exception = assertThrows(ResponseStatusException.class, 
        () -> basketController.addToBasket(testNeed, session));

    assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void testAddToBasket_ExceedsAvailableQuantity() throws IOException {
        lenient().when(session.getAttribute("username")).thenReturn("helper");
        Need testNeed = new Need("Blankets", 10.0, 5, "Supply");
        List<Need> basket = new ArrayList<>();

    
        for (int i = 0; i < 5; i++) {
            basket.add(testNeed);
        }

        when(cupboardDAO.getNeed("Blankets")).thenReturn(testNeed);
        when(session.getAttribute("basket")).thenReturn(basket);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, 
            () -> basketController.addToBasket(testNeed, session));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void testGetBasket_Empty() {
        lenient().when(session.getAttribute("username")).thenReturn("helper");
        when(session.getAttribute("basket")).thenReturn(null); 
        List<Need> result = basketController.getBasket(session);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testRemoveFromBasket_Success() {
        lenient().when(session.getAttribute("username")).thenReturn("helper");
        Need testNeed = new Need("Blankets", 10.0, 5, "Supply");
        List<Need> basket = new ArrayList<>();
        basket.add(testNeed);

        when(session.getAttribute("basket")).thenReturn(basket);

        Map<String, String> response = basketController.removeFromBasket(
            new Need("Blankets", 10.0, 5, "Supply"), session);
        assertEquals("Removed need from basket", response.get("message"));
    }

    @Test
    void testRemoveFromBasket_NotFound() {
        lenient().when(session.getAttribute("username")).thenReturn("helper");
        Need testNeed = new Need("Blankets", 10.0, 5, "Supply");
        List<Need> basket = new ArrayList<>(); 

        when(session.getAttribute("basket")).thenReturn(basket);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, 
            () -> basketController.removeFromBasket(testNeed, session));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void testCheckout_Success() throws IOException {
        lenient().when(session.getAttribute("username")).thenReturn("helper");
        Need testNeed = new Need("Blankets", 10.0, 2, "Supply");
        Need cupboardNeed = new Need("Blankets", 10.0, 5, "Supply");
        List<Need> basket = new ArrayList<>();
        basket.add(testNeed);

        lenient().when(session.getAttribute("username")).thenReturn("helper");
        lenient().when(session.getAttribute("basket")).thenReturn(basket);
        when(cupboardDAO.getNeed("Blankets")).thenReturn(cupboardNeed);
        when(cupboardDAO.updateNeed(eq("Blankets"), any(Need.class))).thenReturn(cupboardNeed);

        Map<String, Object> response = basketController.checkoutBasket(session);
        assertEquals("Checkout successful", response.get("message"));
    }

/**

    @Test
    void testCheckout_FailsDueToInsufficientQuantity() throws IOException {
        lenient().when(session.getAttribute("username")).thenReturn("helper");
        Need testNeed = new Need("Blankets", 10.0, 5, "Supply");
        Need cupboardNeed = new Need("Blankets", 10.0, 2, "Supply");
        
        List<Need> basket = new ArrayList<>();
        basket.add(testNeed);

        lenient().when(session.getAttribute("username")).thenReturn("helper");
        lenient().when(session.getAttribute("basket")).thenReturn(basket);
        when(cupboardDAO.getNeed("Blankets")).thenReturn(cupboardNeed);

        Map<String, Object> response = basketController.checkoutBasket(session);
        assertEquals(false, response.get("success"));
    }
    * 
 */
}
