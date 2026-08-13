package com.tutorialsninja.utils.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class JsonDataProvider {

    private static final ObjectMapper mapper  = new ObjectMapper();
    private static final String BASE_PATH = "src/main/resources/testdata/json/";

    private JsonDataProvider() {}

    public static Map<String, String> getAsMap(String fileName) {
        try {
            return mapper.readValue(resolve(fileName), new TypeReference<>() {});
        } catch (IOException e) {
            throw new RuntimeException("JSON read failed: " + fileName, e);
        }
    }

    public static List<Map<String, String>> getAsList(String fileName) {
        try {
            return mapper.readValue(resolve(fileName), new TypeReference<>() {});
        } catch (IOException e) {
            throw new RuntimeException("JSON read failed: " + fileName, e);
        }
    }

    private static File resolve(String fileName) {
        File f = new File(BASE_PATH + fileName);
        if (!f.exists()) throw new IllegalArgumentException("File not found: " + f.getAbsolutePath());
        return f;
    }
}
