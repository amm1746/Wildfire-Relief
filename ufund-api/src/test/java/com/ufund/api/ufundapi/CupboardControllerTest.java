package com.ufund.api.ufundapi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.ufund.api.ufundapi.controller.CupboardController;
import com.ufund.api.ufundapi.dao.CupboardDAO;
import com.ufund.api.ufundapi.model.Need;

class CupboardControllerTest {

    @Mock
    private CupboardDAO cupboardDAO; // Mocking the DAO interface

    @InjectMocks
    private CupboardController cupboardController; // Controller to test

    private Need existingNeed;
    private Need updatedNeed;

    @BeforeEach
    void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);

        // Create sample Need objects
        existingNeed = new Need("Need1", 10.0, 5, "Food");
        updatedNeed = new Need("Need1", 15.0, 10, "Food");
    }

    @Test
    void testUpdateNeedSuccess() {
        // Simulate the existence of the need
        when(cupboardDAO.needExists(existingNeed.getName())).thenReturn(true);
        when(cupboardDAO.updateNeed(existingNeed.getName(), updatedNeed)).thenReturn(updatedNeed);

        // Call the method to test
        String result = cupboardController.updateNeed(existingNeed.getName(), updatedNeed);

        // Verify the result
        assertEquals("Need updated successfully", result);

        // Verify interactions with the DAO
        verify(cupboardDAO).needExists(existingNeed.getName());
        verify(cupboardDAO).updateNeed(existingNeed.getName(), updatedNeed);
    }

    @Test
    void testUpdateNeedNotFound() {
        // Simulate the need not being found
        when(cupboardDAO.needExists(existingNeed.getName())).thenReturn(false);

        // Call the method to test
        String result = cupboardController.updateNeed(existingNeed.getName(), updatedNeed);

        // Verify the result
        assertEquals("Need not found", result);

        // Verify interactions with the DAO
        verify(cupboardDAO).needExists(existingNeed.getName());
        verify(cupboardDAO, never()).updateNeed(anyString(), any(Need.class));
    }

    @Test
    void testUpdateNeedFailure() {
        // Simulate the need existing but update failing (DAO returns null)
        when(cupboardDAO.needExists(existingNeed.getName())).thenReturn(true);
        when(cupboardDAO.updateNeed(existingNeed.getName(), updatedNeed)).thenReturn(null);

        // Call the method to test
        String result = cupboardController.updateNeed(existingNeed.getName(), updatedNeed);

        // Verify the result
        assertEquals("Failed to update need", result);

        // Verify interactions with the DAO
        verify(cupboardDAO).needExists(existingNeed.getName());
        verify(cupboardDAO).updateNeed(existingNeed.getName(), updatedNeed);
    }
}
