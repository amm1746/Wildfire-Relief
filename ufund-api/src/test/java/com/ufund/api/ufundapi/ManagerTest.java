package com.ufund.api.ufundapi;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ufund.api.ufundapi.dao.CupboardFileDAO;
import com.ufund.api.ufundapi.log.Manager;
import com.ufund.api.ufundapi.model.Need;

public class ManagerTest {

    @InjectMocks
    private Manager manager;

    @Mock
    private CupboardFileDAO needsCupboard;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testViewAllNeeds() {
        when(needsCupboard.getAllNeeds()).thenReturn(List.of(new Need("Need1", 10.0, 1, "Type1"), new Need("Need2", 20.0, 2, "Type2")));
        assertEquals(2, manager.viewAllNeeds().size());
    }

    @Test
    public void testAddNeed() throws IOException {
        Need newNeed = new Need("NewNeed", 15.0, 3, "TypeA");
        manager.addNeed(newNeed);
        verify(needsCupboard, times(1)).createNeed(newNeed);
    }

    @Test
    public void testDeleteNeed() throws IOException {
        when(needsCupboard.needExists("OldNeed")).thenReturn(true); // Add this line
        manager.deleteNeed("OldNeed");
        verify(needsCupboard, times(1)).deleteNeed("OldNeed");
    }

    @Test
    public void testUpdateNeed() throws IOException {
        Need updatedNeed = new Need("UpdatedNeed", 30.0, 5, "TypeB");
        manager.updateNeed("Need1", updatedNeed);
        verify(needsCupboard, times(1)).updateNeed("Need1", updatedNeed);
    }
}
