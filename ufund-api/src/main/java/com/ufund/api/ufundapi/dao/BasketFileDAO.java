package com.ufund.api.ufundapi.dao;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.Need;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 
 */

@Component
public class BasketFileDAO {
    private final ObjectMapper objectMapper;
    private final String filename;
    private Map<String, List<Need>> baskets; 

    public BasketFileDAO(@Value("${baskets.file}") String filename, 
                        ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();
    }

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
    

    private void save() throws IOException {
        objectMapper.writeValue(new File(filename), baskets);
    }

    public List<Need> getBasket(String username) {
        return baskets.getOrDefault(username, new ArrayList<>());
    }

    public void saveBasket(String username, List<Need> items) throws IOException {
        baskets.put(username, items);
        save();
    }
}