package com.kaslanaki.sber;

public class LinkedListExample {
    public static void main(String[] args) {
        SinglyLinkedList list = new SinglyLinkedList();

        list.add(10);
        list.add(25);
        list.add(39);
        list.printList(); // Вывод: 10 -> 25 -> 39 -> null

        list.remove(25);
        list.printList(); // Вывод: 10 -> 39 -> null

        System.out.println("Размер списка: " + list.size()); // Вывод: Размер списка: 2

        System.out.println(list.get(1)); // Ищем второй в списке элемент
    }
}
