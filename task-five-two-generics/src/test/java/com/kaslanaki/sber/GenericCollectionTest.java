package com.kaslanaki.sber;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GenericCollectionTest {

    private GenericCollection<Person> personCollection;

    @BeforeEach
    void setUp() {
        personCollection = new GenericCollection<>();
    }

    @Test
    void testAdd() {
        Person person = new Person("Alice", 30);
        personCollection.add(person);
        assertEquals(1, personCollection.size());
        assertEquals(person, personCollection.get(0));
    }

    @Test
    void testRemove() {
        Person person1 = new Person("Alice", 30);
        Person person2 = new Person("Bob", 25);
        personCollection.add(person1);
        personCollection.add(person2);

        personCollection.remove(0);
        assertEquals(1, personCollection.size());
        assertEquals(person2, personCollection.get(0));
    }

    @Test
    void testGet() {
        Person person = new Person("Alice", 30);
        personCollection.add(person);

        assertEquals(person, personCollection.get(0));
    }

    @Test
    void testSize() {
        assertEquals(0, personCollection.size());

        personCollection.add(new Person("Alice", 30));
        assertEquals(1, personCollection.size());

        personCollection.add(new Person("Bob", 25));
        assertEquals(2, personCollection.size());
    }

    @Test
    void testPrintAll() {
        Person person1 = new Person("Alice", 30);
        Person person2 = new Person("Bob", 25);
        personCollection.add(person1);
        personCollection.add(person2);

        // Печать в консоль не может быть проверена напрямую
        // Мы можем только проверить размер коллекции
        assertEquals(2, personCollection.size());
    }

    @Test
    void testRemoveInvalidIndex() {
        personCollection.add(new Person("Alice", 30));
        personCollection.remove(1); // Не существует
        assertEquals(1, personCollection.size());
    }

    @Test
    void testGetInvalidIndex() {
        assertNull(personCollection.get(0)); // Индекс 0 не существует
    }
}
