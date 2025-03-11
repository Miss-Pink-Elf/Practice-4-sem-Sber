package com.kaslanaki.sber;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        Assertions.assertEquals(2, list.head.data); // Проверяем, что новый голова - 2
    }

    @Test
    public void testRemoveNonExistentElement() {
        list.add(1);
        list.add(2);
        list.remove(3); // Удаляем элемент, которого нет
        Assertions.assertEquals(2, list.size()); // Размер списка не должен измениться
    }

    @Test
    public void testSizeOfEmptyList() {
        Assertions.assertEquals(0, list.size()); // Проверяем, что размер пустого списка равен 0
    }

    @Test
    public void testPrintList() {
        // Печать списка не требует проверки, но можно использовать для визуализации
        list.add(1);
        list.add(2);
        list.add(3);
        list.printList(); // Ожидаемый вывод: 1 -> 2 -> 3 -> null
    }
}
