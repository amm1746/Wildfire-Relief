package com.ufund.api.ufundapi.dao;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.Need;

/**
 * BasketFileDAO 
 * 
 * @author Evan Lin
 */

@Component
public class BasketFileDAO {
    private final ObjectMapper objectMapper;
    private final String filename;
    private Map<String, List<Need>> baskets; 

    /*
     * Construct the basket with whatever is saved.
     */
    public BasketFileDAO(@Value("${baskets.file}") String filename, 
                        ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();
    }

    /*
     * Load saved needs into basket.
     */
    private void load() throws IOException {
        File file = new File(filename);
        if (file.exists()) {
            var typeFactory = objectMapper.getTypeFactory();
            var listType = typeFactory.constructCollectionType(List.class, Need.class);
            var mapType = typeFactory.constructMapType(Map.class, typeFactory.constructType(String.class), listType);
            baskets = objectMapper.readValue(file, mapType);
        } else {
            baskets = new HashMap<>();
        }
    }
    
    /*
     * Saves needs that are in the basket. 
     */
    private void save() throws IOException {
        objectMapper.writeValue(new File(filename), baskets);
    }

    /*
     * Get the basket for the specific username.
     */
    public List<Need> getBasket(String username) {
        return baskets.getOrDefault(username, new ArrayList<>());
    }

    /*
     * Saves the basket to the specific username.
     */
    public void saveBasket(String username, List<Need> items) throws IOException {
        baskets.put(username, items);
        save();
    }

    /*
     * Removes need from the basket.
     */
    public void removeNeedFromAllBaskets(String needName) throws IOException {
    boolean modified = false;
    for (List<Need> basket : baskets.values()) {
        if (basket.removeIf(need -> need.getName().equalsIgnoreCase(needName))) {
            modified = true;
        }
    }
    if (modified) {
        save();
    }
}
}