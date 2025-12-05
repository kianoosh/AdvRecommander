package com.challenge.util;

import com.challenge.model.Click;
import com.challenge.model.Impression;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;
import java.io.File;
import java.util.ArrayList;

public class JsonLoader {
    public static List<Impression> loadImpressions(String filePath) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            List<Impression> result = mapper.readValue(new File(filePath), new TypeReference<List<Impression>>() {
            });
            System.out.println("Loaded impressions: " + result.size());
            return result;
        } catch (Exception e) {
            System.err.println("Error reading impressions: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static List<Click> loadClicks(String filePath) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            List<Click> result = mapper.readValue(new File(filePath), new TypeReference<List<Click>>() {
            });
            System.out.println("Loaded clicks: " + result.size());
            return result;
        } catch (Exception e) {
            System.err.println("Error reading clicks: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}