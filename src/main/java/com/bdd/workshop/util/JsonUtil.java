package com.bdd.workshop.util;

import java.io.IOException;

import com.bdd.workshop.exceptionHandling.CustomRuntimeException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {
    public static String writeAsJsonString(ObjectMapper objectMapper, Object object) {
        try {
            String jsonString = objectMapper.writeValueAsString(object);
            JsonNode jsonNode = objectMapper.readTree(jsonString);
            return jsonNode.toString();
        } catch (IOException e) {
            throw new CustomRuntimeException(
                "JSON_WRITING_ERROR",
                "Failed to write object to JSON string"
            );
        }
    }

    public static <T> T readJson(ObjectMapper objectMapper, String jsonString, Class<T> clazz) {
        try {
            JsonNode jsonNode = objectMapper.readTree(jsonString);
            return objectMapper.readValue(jsonNode.asText(), clazz);
        } catch (IOException e) {
            throw new CustomRuntimeException(
                "JSON_READ_ERROR",
                "Failed to read JSON"
            );
        }
    }
}
