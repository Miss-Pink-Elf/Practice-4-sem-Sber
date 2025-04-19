package com.kaslanaki.sber;

// Главный класс
public class Main {
    public static void main(String[] args) {
        GenericCollection<Person> personCollection = new GenericCollection<>();

        // Добавляем элементы
        personCollection.add(new Person("Алексей", 30));
        personCollection.add(new Person("Мария", 25));
        personCollection.add(new Person("Иван", 40));

        // Вывод размера коллекции
        System.out.println("Размер коллекции: " + personCollection.size());

        // Получаем и выводим элемент по индексу
        Person person = personCollection.get(1);
        System.out.println("Полученный элемент: " + person);

        // Удаляем элемент по индексу
        personCollection.remove(0);
        System.out.println("После удаления элемента:");

        // Выводим все элементы коллекции
        personCollection.printAll();
    }
}
