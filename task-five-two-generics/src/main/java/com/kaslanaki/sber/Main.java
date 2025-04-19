package com.kaslanaki.sber;

public class Main {
    public static void main(String[] args) {
        GenericCollection<Person> personCollection = new GenericCollection<>();

        personCollection.add(new Person("Александр", 57));
        personCollection.add(new Person("Надежда", 19));
        personCollection.add(new Person("Андрей", 20));

        System.out.println("Все элементы в списке:");
        personCollection.printAll();
        System.out.println("---------------------------");

        System.out.println("Размер коллекции: " + personCollection.size());
        System.out.println("---------------------------");

        Person person = personCollection.get(1);
        System.out.println("Второй элемент:\n" + person);
        System.out.println("---------------------------");

        personCollection.remove(0);
        System.out.println("После удаления первого элемента:");
        personCollection.printAll();
    }
}
