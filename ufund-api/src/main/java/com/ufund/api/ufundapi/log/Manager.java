package com.ufund.api.ufundapi.log;
import java.io.IOException;
import java.util.List;
import com.ufund.api.ufundapi.model.Need;

/**
 * Interface for the manager.
 *
 * @author Evan Lin
 */

public interface Manager 
{
    Need create(String name, double cost, int quantity, String type);

}