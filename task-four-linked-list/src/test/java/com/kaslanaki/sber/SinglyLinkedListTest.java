package com.kaslanaki.sber;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

class SinglyLinkedListTest {
    private SinglyLinkedList list;

    @BeforeEach
    public void setUp() {
        list = new SinglyLinkedList(); // Создаем новый список перед каждым тестом
    }

    @Test
    public void testAddSingleElement() {
        list.add(1);
        Assertions.assertEquals(1, list.size()); // Проверяем, что размер списка равен 1
    }

    @Test
    public void testAddMultipleElements() {
        list.add(1);
        list.add(2);
        list.add(3);
        Assertions.assertEquals(3, list.size()); // Проверяем, что размер списка равен 3
    }

    @Test
    public void testRemoveElementFromMiddle() {
        list.add(1);
        list.add(2);
        list.add(3);
        list.remove(2);
        Assertions.assertEquals(2, list.size()); // Проверяем, что размер списка равен 2
    }

    @Test
    public void testRemoveHeadElement() {
        list.add(1);
        list.add(2);
        list.add(3);
        list.remove(1);
        Assertions.assertEquals(2, list.size()); // Проверяем, что размер списка равен 2
        Assertions.assertEquals(2, list.head.data); // Проверяем, что новая голова - 2
    }

    @Test
    public void testRemoveNonExistentElement() {
        list.add(1);
        list.add(2);
        list.remove(3); // Удаляем элемент, которого нет
        Assertions.assertEquals(2, list.size()); // Размер списка не должен измениться
    }

    @Test
    public void testAddOtherTypesData() {
        list.add(1);
        list.add("Трудно быть Богом, Стругацкие");
        list.add(3.56768);

        // Перенаправляем стандартный вывод в поток
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        list.printList();  // Вывод списка

        String expectedOutput = "1 -> Трудно быть Богом, Стругацкие -> 3.56768 -> null\n";
        Assertions.assertEquals(expectedOutput, outputStream.toString().replaceAll("\r", ""));

        // Возвращаем стандартный вывод в исходное состояние
        System.setOut(System.out);
    }

    @Test
    public void testSizeOfEmptyList() {
        Assertions.assertEquals(0, list.size()); // Проверяем, что размер пустого списка равен 0
    }

    @Test
    public void testGetValueByIndex() {
        list.add("Пикник на обочине");
        list.add("Трудно быть Богом");
        list.add("Понедельник начинается в субботу");
        Assertions.assertEquals("Трудно быть Богом", list.get(1));
        Assertions.assertEquals("Понедельник начинается в субботу", list.get(2));
    }
}
