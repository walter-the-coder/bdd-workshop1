package com.bdd.workshop.util;

import java.io.IOException;

import org.springframework.http.HttpStatus;

import com.bdd.workshop.exceptionHandling.CustomRuntimeException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {
    public static String writeJson(ObjectMapper objectMapper, Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (IOException e) {
            throw new CustomRuntimeException(
                "JSON_WRITING_ERROR",
                "Failed to write object to JSON",
                e,
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}
