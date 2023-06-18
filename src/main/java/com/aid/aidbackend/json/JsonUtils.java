package com.aid.aidbackend.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class JsonUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final TypeReference<Map<String, Object>> typeOfMap = new TypeReference<>() {
    };

    public static String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new JsonException(e);
        }
    }

    public static <T> T fromJson(String json, Class<T> type) {
        try {
            return objectMapper.readValue(json, type);
        } catch (JsonProcessingException e) {
            throw new JsonException(e);
        }
    }

    public static Map<String, Object> fromJson(String json) {
        try {
            return objectMapper.readValue(json, typeOfMap);
        } catch (JsonProcessingException e) {
            throw new JsonException(e);
        }
    }

}
