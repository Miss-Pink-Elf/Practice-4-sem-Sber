package com.kaslanaki;

import com.serializer.JsonField;
import com.serializer.JsonSerializer;
import com.serializer.JsonSerializationException;


import java.util.List;

public class App {

    public static class Person {
        @JsonField
        private String name;

        @JsonField(name = "age_years")
        private int age;

        @JsonField
        private List<String> hobbies;

        public Person(String name, int age, List<String> hobbies) {
            this.name = name;
            this.age = age;
            this.hobbies = hobbies;
        }
    }

    public static void main(String[] args) {
        try {
            Person person = new Person("Alice", 30, List.of("Reading", "Hiking"));
            JsonSerializer serializer = new JsonSerializer();
            String json = serializer.serialize(person);
            System.out.println(json);
            // Ожидается вывод: {"name":"Alice", "age_years":30, "hobbies":["Reading", "Hiking"]}
        } catch (JsonSerializationException e) {
            System.err.println("Ошибка сериализации: " + e.getMessage());
        }
    }
}
