package com.serializer;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class JsonSerializer {

    public String serialize(Object object) throws JsonSerializationException {
        if (object == null) return "null";

        Map<String, Object> jsonMap = new HashMap<>();
        Class<?> objClass = object.getClass();

        for (Field field : objClass.getDeclaredFields()) {
            field.setAccessible(true);
            JsonField annotation = field.getAnnotation(JsonField.class);

            if (annotation != null) {
                Object value;
                try {
                    value = field.get(object);
                } catch (IllegalAccessException e) {
                    throw new JsonSerializationException("Error accessing field: " + field.getName(), e);
                }

                if (value != null) {
                    String key = annotation.name().isEmpty() ? field.getName() : annotation.name();
                    jsonMap.put(key, serializeValue(value));
                }
            }
        }
        return convertMapToJson(jsonMap);
    }

    private Object serializeValue(Object value) throws JsonSerializationException {
        if (value instanceof Collection) {
            return serializeCollection((Collection<?>) value);
        } else if (value.getClass().isArray()) {
            return serializeArray(value);
        } else if (value.getClass().isAssignableFrom(String.class) ||
                value.getClass().isPrimitive() || value instanceof Number || value instanceof Boolean) {
            return value;
        } else {
            throw new JsonSerializationException("Unsupported type: " + value.getClass().getName());
        }
    }

    private String serializeCollection(Collection<?> collection) throws JsonSerializationException {
        StringBuilder json = new StringBuilder("[");
        for (Object item : collection) {
            if (json.length() > 1) json.append(",");
            json.append(serializeValue(item));
        }
        json.append("]");
        return json.toString();
    }

    private String serializeArray(Object array) throws JsonSerializationException {
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < java.lang.reflect.Array.getLength(array); i++) {
            if (json.length() > 1) json.append(",");
            json.append(serializeValue(java.lang.reflect.Array.get(array, i)));
        }
        json.append("]");
        return json.toString();
    }

    private String convertMapToJson(Map<String, Object> jsonMap) {
        StringBuilder json = new StringBuilder("{");
        for (Map.Entry<String, Object> entry : jsonMap.entrySet()) {
            if (json.length() > 1) json.append(",");
            json.append("\"").append(entry.getKey()).append("\":").append(formatValue(entry.getValue()));
        }
        json.append("}");
        return json.toString();
    }

    private String formatValue(Object value) {
        if (value instanceof String) {
            return "\"" + value + "\"";
        }
        return value.toString();
    }
}
